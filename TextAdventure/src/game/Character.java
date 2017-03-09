package game;

import java.util.HashMap;
import java.util.Map;

import world.World;

public class Character extends Entity{
	Dialog initialDialog;
	Dialog currentDialog;
	
	public Character(String name, String description, int paramId) {
		super(name, description, paramId);
	}

	public void startDialog(GUI gui, World world, Player player){
		if(initialDialog == null)
			return;
		
		player.beginDialog(this);
		currentDialog = initialDialog;
		currentDialog.writeDown(gui);
		if(currentDialog.dialogBehaviour != null) currentDialog.dialogBehaviour.diag_behave(gui, world, player);
		
		//if(currentDialog.dialogBehaviour != null) currentDialog.dialogBehaviour.diag_behave(gui, world, player);
		
		if(!currentDialog.hasAnswers()){
			System.out.println("hmm");
			player.endDialog();
		}
	}
}
