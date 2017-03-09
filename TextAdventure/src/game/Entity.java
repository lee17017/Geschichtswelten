package game;

import java.util.HashMap;
import java.util.Map;

import world.World;

public class Entity {
	
	public interface IBehaviour {
		public void behave(GUI gui, World world, Player player, int param);
	}
	
	private int id;
	private String name;
	private String description;
	private int paramId;
	private int room;
	private int x, y;
	public boolean hidden;
	private Map<Integer, IBehaviour> behaviours;
	
	public Entity(String name, String description, int paramId){
		behaviours = new HashMap<Integer, IBehaviour>();
		this.name = name;
		this.description = description;
		this.paramId = paramId;
		this.hidden = false;
	}
	
	public void apply(GUI gui, World world, Player player, int command, int param){
		if(behaviours.containsKey(command))
			behaviours.get(command).behave(gui, world, player, param);
	}
	
	public void updateBehaviour(IBehaviour behaviour, int command){
		behaviours.put(command, behaviour);
	}
	
	public boolean setPos(World world, int room, int x, int y){
		if(!world.hasRoom(room))
			return false;
		
		if(!world.hasRoomSection(room, x, y))
			return false;
		
		this.room = room;
		this.x = x;
		this.y = y;
		
		return true;
	}
	
	
	public String getName(){ return name; }
	public String getDescription(){ return description; }
	public int getParamId(){ return paramId; }
	public int getRoom(){ return room; }
	public int getX(){ return x; }
	public int getY(){ return y; }
}
