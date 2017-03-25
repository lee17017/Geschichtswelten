package game;

import java.util.ArrayList;

import world.Location;
import world.World;

public class Player {
	private String name;
	private int location;
	private char orientation;
	private ArrayList<Integer> inventory;
	private boolean isInDialog;
	
	public Player(){
		name = "Player";
		location = -1;
		inventory = new ArrayList<>();
		isInDialog = false;
	}
	
	public boolean Initialize(){
		return true;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean setLocation(GUI gui, World world, int location){
		if(Location.getLocation(location) == null || this.location == location)
			return false;
		
                
                    
                if(this.location == -1 || Location.getLocation(this.location).getSoundID() != Location.getLocation(location).getSoundID())
                {
                    Sound.playBGM(Location.getLocation(location).getSoundID());
                }
                    
                 if(this.location  == -1 || Location.getLocation(this.location).getBildID() != Location.getLocation(location).getBildID())
                {
                    gui.setBg(Location.getLocation(location).getBildID());
                }
                 if(this.location != -1)
                    Location.getLocation(this.location).onExit(gui, world, this);
		this.location = location;
		Location.getLocation(this.location).onEnter(gui, world, this);
		
		return true;
	}
	
	public void beginDialog(){
		this.isInDialog = true;
	}
	
	public void endDialog(){
		//Game.Get().shootEvent(0, currDialogPartner.getName());
		this.isInDialog = false;
	}
	
	public boolean isInDialog(){
		return this.isInDialog;
	}
	
	public boolean addToInventory(Integer e){
		if(inventory.contains(e))
			return false;
		return inventory.add(e);
	}
	
	public boolean removeFromInventory(Integer e){
		return inventory.remove(e);
	}
	
	
	/// Getter
	public String getName(){ return this.name; }
	public int getLocation(){ return this.location; }
	public char getOrientation(){ return this.orientation; }
}
