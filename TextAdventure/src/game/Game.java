package game;

import java.util.ArrayList;
import java.util.HashMap;

import world.Location;
import world.World;

public class Game {
	private boolean isRunning;
	
	private GUI gui;
	
	private ArrayList<Event> events;
	
	public interface IInputCallback{
		public boolean callback(GUI gui, World world, Player player, String input);
	};
	private class InputCallback{
		public boolean hasInput = false;
		public IInputCallback callback;
		public String input;
		public InputCallback(IInputCallback cb){
			hasInput = false;
			callback = cb;
		}
	};
	
	private ArrayList<InputCallback> inputCallbacks;
	
	private World world;
	private Player player;
	private HashMap<Integer, NPC> npcs;
	private int npcCount = 0;
	
	private class StoryText{
		public int wait, delay, currIdx = 0;
		public String header;
		public String[] prefixes;
		public String[] text;
	};
	private ArrayList<StoryText> storyTexts;
	private boolean isWritingStoryText;
	
        
	private Game(){
		isRunning = false;
		events = new ArrayList<Event>();
		inputCallbacks = new ArrayList<InputCallback>();
		world = new World();
		player = new Player();
		npcs = new HashMap<Integer, NPC>();
		npcCount = 0;
		storyTexts = new ArrayList<>();
		isWritingStoryText = false;
	}
	
	/*
	 * GAME LOGIC
	 */
	public void Start(){
		GameResources.init();
		
		if(!world.Initialize())
			return;
		if(!player.Initialize())
			return;

		//TODO: füg die richtigen params hinzu
		addCharacter(NPC.initScarlett(12));
		addCharacter(NPC.initViola(11));
		addCharacter(NPC.initElliot(10));
		addCharacter(NPC.initDean(27));
		gui.setInputMessage("Press Enter");
		writeStoryText(GameResources.prolog_1_header, GameResources.prolog_1, new String[]{}, 35, 200);
		writeStoryText("", GameResources.prolog_2, GameResources.prolog_2_prefixes, 35, 200);
		isRunning = true;
		Run();
	}
	
	public void Run(){
		while(isRunning){
			synchronized(this){
				if(inputCallbacks.size() > 0){
					for(int i = inputCallbacks.size()-1; i >= 0; --i){
						if(inputCallbacks.get(i).hasInput){
							if( inputCallbacks.get(i).callback.callback(gui, world, player, inputCallbacks.get(i).input) )
								inputCallbacks.remove(i);
						}
					}
				}
				
				if(!gui.stillWriting() && isWritingStoryText && storyTexts.get(0).currIdx >= storyTexts.get(0).text.length)
					storyTexts.remove(0);
				
				if(storyTexts.isEmpty() && isWritingStoryText){
                                        gui.setInputMessage("");
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
							if(st.currIdx < st.text.length){
								if(st.prefixes.length > st.currIdx && !st.prefixes[st.currIdx].equals("")){
									gui.writeln(st.prefixes[st.currIdx], 0, 200);
									gui.write(st.text[st.currIdx++], st.wait, st.delay);
								}
								else{
									gui.writeln(st.text[st.currIdx++], st.wait, st.delay);
								}
							}
						}
						else if(!gui.stillWriting() && !isWritingStoryText && player.isInDialog())
							addEvent(3, new String[]{""});
					}
					
					else if(e.type == 2 && e.params.length >= 4){
						int wait;
						int delay;
						int prefixSize;
						
						try{
							wait = Integer.parseInt(e.params[0]);
							delay = Integer.parseInt(e.params[1]);
							prefixSize = Integer.parseInt(e.params[3]);
						} catch(NumberFormatException ex){ return; }
						
						StoryText st = new StoryText();
						st.wait = wait;
						st.delay = delay;
						st.header = e.params[2];
						st.prefixes = new String[prefixSize];
						st.text = new String[e.params.length-(4+prefixSize)];

						for(int j = 0; j < prefixSize; ++j)
							st.prefixes[j] = e.params[j+4];
						
						for(int i = 0; i < st.text.length; ++i)
							st.text[i] = e.params[i+4+prefixSize];
						
						
						storyTexts.add(st);
						
						gui.writeln(st.header, 0, 200);
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
	
	public void OnInput(String input){//System.out.println(input);
		if(inputCallbacks.size() > 0){
			for(int i = 0; i < inputCallbacks.size(); ++i){
				inputCallbacks.get(i).input = input;
				inputCallbacks.get(i).hasInput = true;
			}
			return;
		}
		
		if(isWritingStoryText || input.equals("")){
			if(input.equals("")) addEvent(1, new String[0]);
			return;
		}
		
		if(player.isInDialog()){System.out.println("dafuq");
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
	
	public void addInputCallback(IInputCallback callback){
        new Thread(new Runnable() {
            public void run(){
            	synchronized(Game.Get()) {
            		if(inputCallbacks != null)
            			inputCallbacks.add(new InputCallback(callback));
            	}
            }
        }).start();
	}
	
	public void writeStoryText(String header, String[] text, String[] prefixes, int waitTime, int delay){
		String[] params = new String[text.length+prefixes.length+4];
		params[0] = ""+waitTime;
		params[1] = ""+delay;
		params[2] = header;
		params[3] = ""+prefixes.length;
		for(int j = 0; j < prefixes.length; ++j)
			params[j+4] = prefixes[j];
		
		for(int i = 0; i < text.length; ++i)
			params[i+4+prefixes.length] = text[i];
		
		addEvent(2, params);
	}
	
	// Characters
	public void addCharacter(NPC character){
		npcs.put(npcCount++, character);
	}
	
	public ArrayList<NPC> getCharacters(){
		return new ArrayList<NPC>(npcs.values());
	}
	
	public NPC getCharacterByPreName(String name){
		ArrayList<NPC> npcArr = getCharacters();
		
		for(int i = 0; i < npcArr.size(); ++i){
			if(npcArr.get(i).preName.equalsIgnoreCase(name))
				return npcArr.get(i);
		}
		
		return null;
	}
	
	public NPC getCharacterByName(String preName, String surName){
		ArrayList<NPC> npcArr = getCharacters();
		
		for(int i = 0; i < npcArr.size(); ++i){
			if(npcArr.get(i).preName.equalsIgnoreCase(preName) && npcArr.get(i).surName.equalsIgnoreCase(surName))
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
