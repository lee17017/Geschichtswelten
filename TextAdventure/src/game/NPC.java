package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import world.World;

public class NPC{
	public String preName = "";
	public String surName = "";
	public int param;
	public int location;
	
	private Dialog currentDialog;
	private boolean inDialog;
	
	public NPC(String preName, String surName, int paramId) {
		this.preName = preName;
		this.surName = surName;
		this.param = paramId;
		this.inDialog = false;
	}

	public void setDialog(Dialog dialog){
		if(!inDialog)
			currentDialog = dialog;
	}
	
	public void startDialog(GUI gui, World world, Player player){
		if(currentDialog == null)
			return;
		
		player.beginDialog();
		inDialog = true;
		if(!currentDialog.writeCurrent(gui, world, player)){
			currentDialog.end(gui, world, player);
			player.endDialog();
			inDialog = false;
		}
	}
	
	public void onDialogResponse(GUI gui, World world, Player player, String answer){
		if(!inDialog)
			return;
		
		if(!currentDialog.goTo(gui, world, player, answer)){
			return;
		}
		
		if(!currentDialog.writeCurrent(gui, world, player)){
			currentDialog.end(gui, world, player);
			player.endDialog();
			inDialog = false;
		}
	}
	
	/// Static Helper Functions
	public static NPC initScarlett(int param){
		NPC scarlett = new NPC("Scarlett", "Graham", param);
		return scarlett;
	}
	
	public static NPC initViola(int param){
		NPC viola = new NPC("Viola", "Rose", param);
		viola.setDialog(GameResources.torDialog);
		return viola;
	}
	
	public static NPC initElliot(int param){
		NPC elliot = new NPC("Elliot", "Graham", param);
		elliot.setDialog(GameResources.elliot1Dialog);
		return elliot;
	}
	
	public static NPC initDean(int param){
		NPC dean = new NPC("Dean", "", param);
		dean.setDialog(GameResources.dean1Dialog);
		return dean;
	}
}
