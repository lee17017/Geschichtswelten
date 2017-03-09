package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class World {
	private static int numRooms = 0;
	private Map<Integer, Room> rooms;
	
	public World(){
		this.rooms = new HashMap<Integer, Room>();
	}
	
	public int createRoom(String name, int paramId){
		rooms.put( numRooms, new Room(name, paramId) );
		return numRooms++;
	}
	
	public void addRoomSection(int roomId, int x, int y){
		if(rooms.containsKey(roomId))
			rooms.get(roomId).addSection(x, y, new Section());
	}
	
	public void addRoomExit(int roomId, int x, int y, int toRoom, int destX, int destY, char direction){
		if(rooms.containsKey(roomId))
			rooms.get(roomId).addExit(x, y, toRoom, destX, destY, direction);
	}
	
	public String getRoomName(int roomId){
		if(rooms.containsKey(roomId))
			return rooms.get(roomId).name;
		return new String("");
	}
	
	public int getRoomParamId(int roomId){
		if(rooms.containsKey(roomId))
			return rooms.get(roomId).paramId;
		return -1;
	}
	
	public boolean hasRoom(int roomId){
		return rooms.containsKey(roomId);
	}
	
	public void addRoomSectionCallback(int roomId, int x, int y, Section.ICommandCallback callback){
		if(!hasRoom(roomId))
			return;
		if(!hasRoomSection(roomId, x, y))
			return;
		
		rooms.get(roomId).addSectionCallback(x, y, callback);
	}
	
	public boolean hasRoomSection(int roomId, int x, int y){
		if(rooms.containsKey(roomId))
			return rooms.get(roomId).hasSection(x, y);
		else
			return false;
	}
	
	public Section getRoomSection(int roomId, int x, int y){
		if(rooms.containsKey(roomId)){
			return rooms.get(roomId).getSection(x, y);
		}
		return null;
	}
	
	public ArrayList<Room.Exit> getRoomExits(int roomId, int x, int y){
		if(rooms.containsKey(roomId))
			return rooms.get(roomId).getExit(x, y);
		return null;
	}
}
