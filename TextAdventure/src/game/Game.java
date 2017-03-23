package game;

import java.util.ArrayList;
import java.util.HashMap;

import world.Location;
import world.World;

public class Game {
	private boolean isRunning;
	
	private GUI gui;
	
	private ArrayList<Event> events;
	private World world;
	private Player player;
	private HashMap<Integer, NPC> npcs;
	
	private class StoryText{
		public int wait, delay, currIdx = 0;
		public String[] text;
	};
	private ArrayList<StoryText> storyTexts;
	private boolean isWritingStoryText;
	
	
	private Game(){
		isRunning = false;
		events = new ArrayList<Event>();
		world = new World();
		player = new Player();
		npcs = new HashMap<Integer, NPC>();
		storyTexts = new ArrayList<>();
		isWritingStoryText = false;
	}
	
	/*
	 * GAME LOGIC
	 */
	public void Start(){
		if(!world.Initialize())
			return;
		if(!player.Initialize())
			return;
		
		isRunning = true;
		Run();
	}
	
	public void Run(){
		while(isRunning){
			synchronized(this){
				if(!gui.stillWriting() && isWritingStoryText && storyTexts.get(0).currIdx >= storyTexts.get(0).text.length)
					storyTexts.remove(0);
				
				if(storyTexts.isEmpty() && isWritingStoryText){
					gui.enableWriting(true);
					isWritingStoryText = false;
				}
				
				if(!events.isEmpty()){
					Event e = events.remove(0);
					
					if(e.type == 0 && e.params.length == 3 && !isWritingStoryText){
						int command = Integer.parseInt(e.params[0]);
						int param = Integer.parseInt(e.params[1]);
						int attribute = Integer.parseInt(e.params[2]);
						Location.getLocation(player.getLocation()).onCommand(gui, player, world, command, param, attribute);
					}
					
					else if(e.type == 1){
						if(gui.stillWriting())
							gui.forceWrite(false);
						else if(!gui.stillWriting() && isWritingStoryText){
							StoryText st = storyTexts.get(0);
							if(st.currIdx < st.text.length)
								gui.writeln(st.text[st.currIdx++], st.wait, st.delay);
						}
					}
					
					else if(e.type == 2 && e.params.length > 2){
						int wait;
						int delay;
						
						try{
							wait = Integer.parseInt(e.params[0]);
							delay = Integer.parseInt(e.params[1]);
						} catch(NumberFormatException ex){ return; }
						
						StoryText st = new StoryText();
						st.wait = wait;
						st.delay = delay;
						st.text = new String[e.params.length-2];
						
						for(int i = 2; i < e.params.length; ++i)
							st.text[i-2] = e.params[i];
						
						storyTexts.add(st);
						
						gui.enableWriting(false);
						isWritingStoryText = true;
					}
					
					else if(e.type == 3 && e.params.length == 1){
						ArrayList<NPC> npcArr = getCharacters();
						for(int i = 0; i < npcArr.size(); ++i)
							npcArr.get(i).onDialogResponse(gui, world, player, e.params[0]);
					}
				}
			}
		}
	}
	
	public void OnInput(String input){
		if(input.equals("") || isWritingStoryText){
			addEvent(1, new String[0]);
			return;
		}
		
		if(player.isInDialog()){
			String[] inp = new String[1];
			inp[0] = input;
			addEvent(3, inp);
			return;
		}
		
		if(gui.stillWriting())
			return;
		
		int command = Parser.command(input);
		int param = Parser.param(input);
		int attribute = Parser.attribute(input);
		String[] arr = new String[3];
		arr[0] = ""+command;
		arr[1] = ""+param;
		arr[2] = ""+attribute;
		
		addEvent(0, arr);
	}
	
	public void addEvent(int type, String[] params){
        new Thread(new Runnable() {
            public void run(){
            	synchronized(Game.Get()) {
	        		if(events != null)
	        			events.add(new Event(type, params));
            	}
            }
        }).start();
	}
	
	public void writeStoryText(String[] text, int waitTime, int delay){
		String[] params = new String[text.length+2];
		params[0] = ""+waitTime;
		params[1] = ""+delay;
		for(int i = 0; i < params.length; ++i)
			params[i+2] = text[i];
		
		addEvent(2, params);
	}
	
	// Characters
	public ArrayList<NPC> getCharacters(){
		return new ArrayList<NPC>(npcs.values());
	}
	
	public NPC getCharacterByPreName(String name){
		ArrayList<NPC> npcArr = getCharacters();
		
		for(int i = 0; i < npcArr.size(); ++i){
			if(npcArr.get(i).preName == name)
				return npcArr.get(i);
		}
		
		return null;
	}
	
	public NPC getCharacterByName(String preName, String surName){
		ArrayList<NPC> npcArr = getCharacters();
		
		for(int i = 0; i < npcArr.size(); ++i){
			if(npcArr.get(i).preName == preName && npcArr.get(i).surName == surName)
				return npcArr.get(i);
		}
		
		return null;
	}
	
	
	// Gui
	public void setGUI(GUI gui){
		this.gui = gui;
	}
	
	// Singleton
	private static Game instance;	
	public static Game Get(){
		if(instance == null)
			instance = new Game();
		return instance;
	}
}
