package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {
	public String name;
	public int paramId;
	
	private class SectionIndex { 
		public int x; public int y; 
		public SectionIndex(int x, int y){ this.x = x; this.y = y; }
		
		@Override
		public boolean equals(Object si){
			if(((SectionIndex)si).x == x && ((SectionIndex)si).y == y)
				return true;
			return false;
		}
		
		@Override 
		public int hashCode(){
			return x+y;
		}
	}
	
	public class Exit {
		public int toRoom;
		public int destX, destY;
		public char direction;
		
		public Exit(int to, int x, int y, char dir){ 
			this.toRoom = to; 
			this.destX = x; 
			this.destY = y; 
			this.direction = dir;
		}
	}
	private Map<SectionIndex, Section> sectionMap;
	private Map<SectionIndex, ArrayList<Exit>> exitMap;
	
	public Room(String name, int paramId){
		this.name = name;
		this.sectionMap = new HashMap<SectionIndex, Section>();
		this.exitMap = new HashMap<SectionIndex, ArrayList<Exit>>();
		this.paramId = paramId;
	}
	
	public void addSection(int x, int y, Section s){
		SectionIndex si = new SectionIndex(x,y);
		if( !sectionMap.containsKey(si) )
			sectionMap.put(si, s);
	}
	
	public void addSectionCallback(int x, int y, Section.ICommandCallback callback){
		SectionIndex si = new SectionIndex(x,y);
		if( sectionMap.containsKey(si) )
			sectionMap.get(si).setCallback(callback);
	}
	
	public void addExit(int x, int y, int toRoom, int destX, int destY, char direction){
		SectionIndex si = new SectionIndex(x,y);
		if( !exitMap.containsKey(si) )
			exitMap.put(si, new ArrayList<>());
		exitMap.get(si).add(new Exit(toRoom, destX, destY, direction));
	}
	
	public boolean hasSection(int x, int y){
		return sectionMap.containsKey(new SectionIndex(x,y));	
	}
	
	public Section getSection(int x, int y){
		SectionIndex si = new SectionIndex(x,y);
		if( sectionMap.containsKey(si) ){
			return sectionMap.get(si);
		}
		return null;	
	}
	
	public ArrayList<Exit> getExit(int x, int y){
		SectionIndex si = new SectionIndex(x,y);
		if( exitMap.containsKey(si) )
			return exitMap.get(si);
		return null;
	}
}
