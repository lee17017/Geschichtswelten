
package game;

import java.util.ArrayList;

import world.World;

public class Player {
	private int room;		// room id
	private int xPos, yPos;	// relative to room
	private char orientation;
	private ArrayList<Entity> inventory;
	private boolean isInDialog;
	private Character currDialogPartner;
	
	public Player(){
		inventory = new ArrayList<>();
		isInDialog = false;
		currDialogPartner = null;
	}
	
	public boolean setState(World world, int room, int x, int y, char orientation){
		if(!world.hasRoom(room))
			return false;
		
		if(!world.hasRoomSection(room, x, y))
			return false;
		
		this.room = room;
		this.xPos = x;
		this.yPos = y;
		this.orientation = orientation;
		
		return true;
	}
	
	public boolean goForward(World world){
		int x = xPos;
		int y = yPos;
		
		if(orientation == 'n') y++;
		else if(orientation == 'e') x++;
		else if(orientation == 's') y--;
		else if(orientation == 'w') x--;
		
		return setState(world, this.room, x, y, this.orientation);
	}
	
	public boolean goBack(World world){
		int x = xPos;
		int y = yPos;
		
		if(orientation == 'n') y--;
		else if(orientation == 'e') x--;
		else if(orientation == 's') y++;
		else if(orientation == 'w') x++;
		
		return setState(world, this.room, x, y, this.orientation);
	}
	
	public boolean goRight(World world){
		int x = xPos;
		int y = yPos;
		
		if(orientation == 'n') x++;
		else if(orientation == 'e') y--;
		else if(orientation == 's') x--;
		else if(orientation == 'w') y++;
		
		return setState(world, this.room, x, y, this.orientation);
	}
	
	public boolean goLeft(World world){
		int x = xPos;
		int y = yPos;
		
		if(orientation == 'n') x--;
		else if(orientation == 'e') y++;
		else if(orientation == 's') x++;
		else if(orientation == 'w') y--;
		
		return setState(world, this.room, x, y, this.orientation);		
	}
	
	public void beginDialog(Character c){
		this.isInDialog = true;
		this.currDialogPartner = c;
	}
	
	public void endDialog(){
		//Game.Get().shootEvent(0, currDialogPartner.getName());
		this.isInDialog = false;
		this.currDialogPartner = null;
	}
	
	public Character getDialogPartner(){
		return currDialogPartner;
	}
	
	public boolean isInDialog(){
		return this.isInDialog;
	}
	
	public boolean addToInventory(Entity e){
		if(inventory.contains(e))
			return false;
		return inventory.add(e);
	}
	
	public boolean removeFromInventory(Entity e){
		return inventory.remove(e);
	}
	
	public int getX(){ return this.xPos; }
	public int getY(){ return this.yPos; }
	public int getRoom(){ return this.room; }
	public char getOrientation(){ return this.orientation; }
}
