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
	
	public void startDialog(GUI gui, Player player){
		if(currentDialog == null)
			return;
		
		player.beginDialog();
		currentDialog.writeCurrent(gui);
		inDialog = true;
	}
	
	public void onDialogResponse(GUI gui, World world, Player player, String answer){
		if(!inDialog)
			return;
		
		int ans;
		try{
			ans = Integer.parseInt(answer);
		} catch(NumberFormatException ex){
			gui.writeln("Sie müssen eine gültige Zahl eingeben.", 0, 200);
			return;
		}
		
		if(!currentDialog.goTo(gui, world, player, ans)){
			gui.writeln("Sie müssen eine gültige Zahl eingeben.", 0, 200);
			return;
		}
		
		if(!currentDialog.writeCurrent(gui)){
			currentDialog.end(gui, world, player);
			player.endDialog();
			inDialog = false;
		}
	}
}
