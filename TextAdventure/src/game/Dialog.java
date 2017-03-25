package game;

import java.util.ArrayList;
import java.util.HashMap;

import world.World;

public class Dialog {
	public interface IDialogCallback{
		public void callback(GUI gui, World world, Player player, DialogText dialogText);
	};
	
	public class DialogText{
		DialogText(String characterName){
			this.characterName = characterName;
			this.characterTexts = new ArrayList<>();
			this.nextDialogTexts = new ArrayList<>();
			startCallback = null;
		}
		
		String characterName;
		ArrayList<String> characterTexts;
		ArrayList<Integer> nextDialogTexts;
		IDialogCallback startCallback;
	};
	
	HashMap<Integer, DialogText> dialogTexts;
	private int dialogTextCount;
	private int currentDialogText;
	IDialogCallback endCallback;
	
	
	public Dialog(){
		this.dialogTextCount = 0;
		this.dialogTexts = new HashMap<Integer, DialogText>();
		this.currentDialogText = 0;
		this.endCallback = null;
	}
	
	public int createDialogText(String characterName){
		DialogText dt = new DialogText(characterName);
		dialogTexts.put(dialogTextCount, dt);
		return dialogTextCount++;
	}
	
	public void setCharacterText(int parent, int child, String text){
		if(!dialogTexts.containsKey(parent)) 
			return;
		
		dialogTexts.get(parent).characterTexts.add(text);
		dialogTexts.get(parent).nextDialogTexts.add(child);
	}
	
	public void setDialogTextCallback(IDialogCallback callback, int dialogText){
		if(!dialogTexts.containsKey(dialogText))
			return;
		
		dialogTexts.get(dialogText).startCallback = callback;
	}
	
	public boolean setCurrentDialogText(int dt){
		if(!dialogTexts.containsKey(dt))
			return false;
		
		currentDialogText = dt;
		return true;
	}
	
	public boolean goTo(GUI gui, World world, Player player, String answer){
		DialogText dt = dialogTexts.get(currentDialogText);
		
		int ans;
		
		if(dt.characterTexts.size() == 1){
			if(!answer.equals("")){
				gui.writeln(GameResources.defaultYouAreInAConversationText, 0, 200);
				return false;
			}
			
			ans = 0;
		}
		else{
			try{
				ans = Integer.parseInt(answer);
			} catch(NumberFormatException ex){
				gui.writeln(GameResources.defaultYouAreInAConversationText, 0, 200);
				return false;
			}
			
			if(ans >= dt.nextDialogTexts.size()){
				gui.writeln(GameResources.defaultInvalidNumberInDiscussionText, 0, 200);
				return false;
			}
		}

		currentDialogText = dt.nextDialogTexts.get(ans);
		
		return true;
	}
	
	public boolean writeCurrent(GUI gui, World world, Player player){
		if(currentDialogText <= -1)
			return false;
		
		DialogText dt = dialogTexts.get(currentDialogText);
		
		if(dt.characterTexts.size() == 0)
			return false;
		
		if(dt.characterTexts.size() == 1){
			gui.writeln(dt.characterName+": ", 0, 200);
			gui.write("„"+dt.characterTexts.get(0)+"“", 25, 200);
		}
		else{
			gui.writeln(dt.characterName+": ", 0, 200);
			for(int i = 0; i < dt.characterTexts.size(); ++i)
				gui.writeln("["+i+"]"+"„"+dt.characterTexts.get(i)+"“", 0, 0);
		}
		
		if(dt.startCallback != null)
			dt.startCallback.callback(gui, world, player, dt);
		
		if(dt.characterTexts.size() == 1 && dt.nextDialogTexts.get(0) <= -1){
			return false;
		}
		
		return true;
	}
	
	public void setEndCallback(IDialogCallback callback){
		this.endCallback = callback;
	}
	
	public void end(GUI gui, World world, Player player){
		if(endCallback != null) endCallback.callback(gui, world, player, null);
		gui.setInputMessage("");
		gui.enableWriting(true);
	}
}
