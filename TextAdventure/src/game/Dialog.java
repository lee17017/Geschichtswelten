package game;

import java.util.ArrayList;
import java.util.HashMap;

import world.World;

public class Dialog {
	public interface IDialogCallback{
		public void callback(GUI gui, World world, Player player);
	};
	
	public class DialogText{
		DialogText(String npcText){
			this.answers = new ArrayList<>();
			this.correspondingDialogTexts = new ArrayList<>();
			this.answerCallbacks = new HashMap<>();
			characterText = npcText;
		}
		
		String characterText;
		ArrayList<String> answers;
		ArrayList<Integer> correspondingDialogTexts;
		HashMap<Integer, IDialogCallback> answerCallbacks;
	};
	
	
	private String characterName;
	HashMap<Integer, DialogText> dialogTexts;
	private int dialogTextCount;
	private int currentDialogText;
	IDialogCallback endCallback;
	
	
	public Dialog(String characterName){
		this.dialogTextCount = 0;
		this.characterName = characterName;
		this.dialogTexts = new HashMap<Integer, DialogText>();
		this.currentDialogText = 0;
		this.endCallback = null;
	}
	
	public boolean setCurrentDialogText(int dt){
		if(!dialogTexts.containsKey(dt))
			return false;
		
		currentDialogText = dt;
		return true;
	}
	
	public boolean goTo(GUI gui, World world, Player player, int answer){
		DialogText dt = dialogTexts.get(currentDialogText);
		if(answer >= dt.answers.size())
			return false;
		
		if(dt.answerCallbacks.containsKey(answer))
			dt.answerCallbacks.get(answer).callback(gui, world, player);
		
		currentDialogText = dt.correspondingDialogTexts.get(answer);
		return true;
	}
	
	public boolean writeCurrent(GUI gui){
		if(currentDialogText <= -1)
			return false;
		
		DialogText dt = dialogTexts.get(currentDialogText);
		gui.writeln(characterName, 0, 200);
		gui.writeln(dt.characterText, 25, 200);
		
		if(dt.answers.size() == 0)
			return false;
		
		for(int i = 0; i < dt.answers.size(); ++i)
			gui.writeln("["+i+"]"+dt.answers.get(i), 0, 0);
		
		return true;
	}
	
	public void setEndCallback(IDialogCallback callback){
		this.endCallback = callback;
	}
	
	public void end(GUI gui, World world, Player player){
		endCallback.callback(gui, world, player);
	}
	
	public int createDialogText(String npcText){
		dialogTexts.put(dialogTextCount, new DialogText(npcText));
		return dialogTextCount++;
	}
	
	public boolean addDialogTextAnswer(int parent, int child, String answer){
		if(!dialogTexts.containsKey(parent))
			return false;
		
		DialogText dt = dialogTexts.get(parent);
		dt.answers.add(answer);
		dt.correspondingDialogTexts.add(child);
		
		return true;
	}
	
	public void setDialogTextCallback(IDialogCallback callback, int dialogText, int answer){
		if(!dialogTexts.containsKey(dialogText) || dialogTexts.get(dialogText).answers.size() <= answer)
			return;
		
		dialogTexts.get(dialogText).answerCallbacks.put(answer, callback);
	}
}
