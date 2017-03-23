package game;

public class Event {
	public int type;	//0->command; 1->enter 2->storytext; 3->dialog
	public String[] params;
	
	public Event(int type, String[] params){
		this.type = type;
		this.params = params;
	}
}
