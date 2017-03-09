package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import world.Room;
import world.World;

public abstract class GameState {
	public interface ICheckpoint {
		public boolean check_and_update(String command, String[] args);
	}
	
	protected World world;
	protected Player player;
	private static int entityCount = 0;
	protected Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	protected ArrayList<ICheckpoint> checkpoints;
	
	protected boolean finished = false;
	
	
	public GameState(World world, Player player, Map<Integer, Entity> entities){
		this.world = world;
		this.player = player;
		this.entities = entities;
		this.finished = false;
	}
	
	public boolean OnCommand(GUI gui, int command, int param){
		boolean worked = false;
		System.out.println(command);
		System.out.println(param);
		
		if(command == 10){
			if(player.getDialogPartner() != null)
				player.getDialogPartner().apply(gui, world, player, command, param);
		}
		else{
			if(command == 0 && (param == 0 || param == 1 || param == 2 || param == 3)){
				switch(param){
				case 0:
					worked = player.goForward(world);
					break;
				case 2:
					worked = player.goRight(world);
					break;
				case 1:
					worked = player.goBack(world);
					break;
				case 3:
					worked = player.goLeft(world);
					break;
				}
			}
			
			else if(command == 5 && param == -1){
				if(world.getRoomSection(player.getRoom(), player.getX(), player.getY()) != null)
					world.getRoomSection(player.getRoom(), player.getX(), player.getY()).callback.callback(gui, world, player, command, param, world.getRoomParamId(player.getRoom()));
				worked = true;
			}
			
			else if(command != -1 && param != -1){
				ArrayList<Integer> indices = new ArrayList<>();
				for(int i = 0; i < entities.size(); ++i){
					Entity e = entities.get(i);
					if(e.getRoom() == player.getRoom() && e.getX() == player.getX() && e.getY() == player.getY() && param == e.getParamId() && !e.hidden)
						indices.add(i);
				}
				for(int j = 0; j < indices.size(); ++j){
					entities.get(indices.get(j)).apply(gui, world, player, command, param);
	
				}
				world.getRoomSection(player.getRoom(), player.getX(), player.getY()).callback.callback(gui, world, player, command, param, world.getRoomParamId(player.getRoom()));
				worked = true;
			}
		}
		return worked;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public abstract void OnStart(GUI gui);
	public abstract void OnUpdate(GUI gui, Event event);
	public abstract void OnEnterPressed(GUI gui);
	public abstract void AfterCommand(GUI gui, int command, int param, boolean successful);
	public abstract void OnFinish(GUI gui);
	public abstract void OnEvent(GUI gui, int event, String from);
	public abstract GameState nextState();
	
	
	
	public int createEntity(String name, String description, int paramId){
		Entity ent = new Entity(name, description, paramId);
		this.entities.put(entityCount, ent);
		
		return entityCount++;
	}
	
	public boolean setEntityPos(int id, World world, int roomId, int x, int y){
		if(entities.containsKey(id))
			return entities.get(id).setPos(world, roomId, x, y);
		return false;
	}
	
	public Entity getEntity(int id){
		if(entities.containsKey(id))
			return entities.get(id);
		return null;
	}
	
	
	/** helper functions **/
	public int createCharacter(String name, String description, int paramId){
		Entity ent = new Character(name, description, paramId);
		ent.updateBehaviour( new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				((Character)ent).startDialog(gui, world, player);
			}
		}, 1);
		
		ent.updateBehaviour( new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				if(((Character)ent).currentDialog.outcomes.containsKey(param)){
					((Character)ent).currentDialog = ((Character)ent).currentDialog.outcomes.get(param);
					((Character)ent).currentDialog.writeDown(gui);
					if(((Character)ent).currentDialog.dialogBehaviour != null) ((Character)ent).currentDialog.dialogBehaviour.diag_behave(gui, world, player);
					if(((Character)ent).currentDialog.outcomes.size() == 0) player.endDialog();
				}
				else{
					player.endDialog();
				}
			}
		}, 10);
		
		
		entities.put(entityCount, ent);
		
		return entityCount++;
	}

	public void setCharacterDialog(int entityId, Dialog d){
		if(entities.containsKey(entityId))
			((Character) entities.get(entityId)).initialDialog = d;
	}
	
	public int[] createDoor(int from, int src_x, int src_y, String desc1, int to, int dst_x, int dst_y, String desc2, char direction){
		if( !world.hasRoomSection(from, src_x, src_y) || !world.hasRoomSection(to, dst_x, dst_y) )
			return null;
		
		Door door1 = new Door(desc1, desc1, 30);
		Door door2 = new Door(desc2, desc2, 30);
		
		door1.setPos(world, from, src_x, src_y);
		door2.setPos(world, to, dst_x, dst_y);
		
		door1.direction = direction;
		if(direction == 'n') door2.direction = 's';
		if(direction == 's') door2.direction = 'n';
		if(direction == 'e') door2.direction = 'w';
		if(direction == 'w') door2.direction = 'e';
		
		
		// door 1
		door1.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				player.setState(world, to, dst_x, dst_y, door1.direction);
				gui.writeln(desc1, 0, 200);
			}
		}, 3);
		
		door1.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln("Die Tür gehört Ihnen nicht!", 0, 200);
			}
		}, 2);
		
		door1.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln(door1.getDescription(), 0, 200);
			}
		}, 5);	
		
		door1.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln("Was haben Sie mit der Tür vor? Wohin wollen Sie die Tür tragen?", 0, 200);
			}
		}, 6);
		
		
		// door 2
		door2.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				player.setState(world, from, src_x, src_y, door2.direction);
				gui.writeln(desc2, 0, 200);
			}
		}, 3);
		
		door2.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln("Die Tür gehört Ihnen nicht!", 0, 200);
			}
		}, 2);
		
		door2.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln(door2.getDescription(), 0, 200);
			}
		}, 5);
		
		door2.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln("Was haben Sie mit der Tür vor? Wohin wollen Sie die Tür tragen?", 0, 200);
			}
		}, 6);
		
		int[] ret = { entityCount, entityCount+1 };
		entities.put(entityCount++, door1);
		entities.put(entityCount++, door2);
		
		return ret;
	}
}
