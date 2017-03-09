package world;

import game.GUI;
import game.Player;

public class Section {
	
	public interface ICommandCallback{
		public void callback(GUI gui, World world, Player player, int command, int param, int self);
	}
	
	
	public ICommandCallback callback;
	
	public Section(){
		callback = new ICommandCallback(){
			@Override
			public void callback(GUI gui, World world, Player player, int command, int param, int self) {
				
			}
		};
	}
	
	public void setCallback(ICommandCallback cb){
		callback = cb;
	}
}
