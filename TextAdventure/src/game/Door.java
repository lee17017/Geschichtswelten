package game;

public class Door extends Entity{
	private int toRoom;
	private int destX, destY;
	public char direction;
	
	public Door(String name, String description, int paramId) {
		super(name, description, paramId);
	}

}
