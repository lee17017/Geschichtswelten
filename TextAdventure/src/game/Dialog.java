package game;

import java.util.HashMap;
import java.util.Map;

import world.World;

public class Dialog {
	public interface IDialogBehaviour {
		public void diag_behave(GUI gui, World world, Player player);
	}
	
	public String characterName = "";
	public String[] characterText;
	public String[][] possibleAnswers;
	public Map<Integer, Dialog> outcomes;
	public IDialogBehaviour dialogBehaviour;
	
	public Dialog(){
		outcomes = new HashMap<Integer, Dialog>();
		dialogBehaviour = null;
	}
	
	public void writeDown(GUI gui){
		gui.writeln(characterName+":", 0, 200);
		
		for(int i = 0; i < characterText.length; ++i)
			gui.writeln(characterText[i], 30, 200);
		
		for(int j = 0; j < possibleAnswers.length; ++j){
			for(int k = 0; k < possibleAnswers[j].length; ++k){
				gui.writeln("["+j+"] " + possibleAnswers[j][k], 0, 200);
			}
		}
	}
	
	public boolean hasAnswers(){
		if(possibleAnswers == null || possibleAnswers.length == 0)
			return false;
		return true;
	}
	
	public void setDialogBehaviour(IDialogBehaviour behaviour){
		dialogBehaviour = behaviour;
	}
	
	public String[][] getPossibleAnswers(){ return possibleAnswers; }
}
