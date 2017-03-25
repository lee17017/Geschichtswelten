package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import game.GUI;
import game.Game;
import game.GameResources;
import game.Player;

public class Location {
	public interface ICommandCallback {
		public void callback(GUI gui, World world, Player player, Location location, int param, int attribute);
	};
	
	public interface IEnterExitCallback {
		public void callback(GUI gui, World world, Player player, Location location);
	}
	
	///--- Static Methods and Attributes
	private static HashMap<Integer, Location> locationMap = new HashMap<>();
	private static int locationCounter = 0;
	private static HashMap<Integer, ICommandCallback> defaultCommandCallbacksNoParams = new HashMap<Integer, ICommandCallback>();
	private static HashMap<Integer, HashMap<Integer, ICommandCallback>> defaultCommandCallbacksParams = new HashMap<Integer, HashMap<Integer, ICommandCallback>>();
	private static IEnterExitCallback defaultEnterCallback;
	private static IEnterExitCallback defaultExitCallback;
	
	public static int createLocation(String name, int bildID, int soundID, String enterText, String exitText, String observeText){
		locationMap.put(locationCounter, new Location(name, bildID, soundID, enterText, exitText, observeText));
		return locationCounter++;
	}
	public static int createLabyrinth(String name, int bildID, int soundID, String enterText, String exitText, String observeText){
		locationMap.put(locationCounter, new LabyrinthField(name, bildID, soundID, enterText, exitText, observeText));
		return locationCounter++;
	}
	public static void setDefaultCallback(int command, ICommandCallback callback){
		defaultCommandCallbacksNoParams.put(command, callback);
	}
	
	public static void setDefaultCallback(int[] command, ICommandCallback callback){
		for(int i = 0; i < command.length; ++i)
			defaultCommandCallbacksNoParams.put(command[i], callback);
	}
	
	public static void setDefaultCallback(int command, int param, ICommandCallback callback){
		if(!defaultCommandCallbacksParams.containsKey(command))
			defaultCommandCallbacksParams.put(command, new HashMap<Integer, ICommandCallback>());
		
		defaultCommandCallbacksParams.get(command).put(param, callback);
	}
	
	public static void setDefaultCallback(int[] command, int param, ICommandCallback callback){
		for(int i = 0; i < command.length; ++i){
			if(!defaultCommandCallbacksParams.containsKey(command[i]))
				defaultCommandCallbacksParams.put(command[i], new HashMap<Integer, ICommandCallback>());
			defaultCommandCallbacksParams.get(command[i]).put(param, callback);
		}
	}
	
	public static void setDefaultEnterCallback(IEnterExitCallback callback){
		defaultEnterCallback = callback;
	}
	
	public static void setDefaultExitCallback(IEnterExitCallback callback){
		defaultExitCallback = callback;
	}
	
	public static Location getLocation(int location){
		if(!locationMap.containsKey(location))
			return null;
		return locationMap.get(location);
	}
	
	
	///--- Object Methods and Attributes
	private String name = "";
	private String enterText = "";
	private String exitText = "";
	private String observeText = "";
	
        private int bildID, soundID;
	private HashMap<Integer, ArrayList<Integer>> adjacentLocations;				//param, [locations]
	private HashMap<Integer, ArrayList<Integer>> adjacentLocationAttributes;	//location, [attributes]
	private HashMap<Integer, HashMap<Integer, ICommandCallback>> callbacks;			//command, param, callbackFunc
	private IEnterExitCallback enterCallback = null;
	private IEnterExitCallback exitCallback = null;
	
	
	// Constructor
	protected Location(String name, int bildID, int soundID, String enterText, String exitText, String observeText){
		adjacentLocations = new HashMap<Integer, ArrayList<Integer>>();
		adjacentLocationAttributes = new HashMap<Integer, ArrayList<Integer>>();
		callbacks = new HashMap<Integer, HashMap<Integer, ICommandCallback>>();
		this.enterText = enterText;
		this.exitText = exitText;
		this.observeText = observeText;
                this.soundID = soundID;
                this.bildID = bildID;
	}
	

	// Behaviour
	public void onCommand(GUI gui, Player player, World world, int command, int param, int attribute){
		if(callbacks.containsKey(command) && callbacks.get(command).containsKey(param)){
			callbacks.get(command).get(param).callback(gui, world, player, this, param, attribute);
			return;
		}
		
		//default behaviour
		if(defaultCommandCallbacksParams.containsKey(command) && defaultCommandCallbacksParams.get(command).containsKey(param)){
			defaultCommandCallbacksParams.get(command).get(param).callback(gui, world, player, this, param, attribute);
			return;
		}
		
		if(defaultCommandCallbacksNoParams.containsKey(command)){
			defaultCommandCallbacksNoParams.get(command).callback(gui, world, player, this, param, attribute);
			return;
		}
		
		gui.writeln(GameResources.defaultUnknownCommandText, 0, 200);
	}
	
	public void onEnter(GUI gui, World world, Player player){
		if(enterCallback != null){
			enterCallback.callback(gui, world, player, this);
			return;
		}
		if(defaultEnterCallback != null) defaultEnterCallback.callback(gui, world, player, this);
	}

	public void onExit(GUI gui, World world, Player player){
		if(exitCallback != null){
			exitCallback.callback(gui, world, player, this);
			return;
		}
		if(defaultExitCallback != null) defaultExitCallback.callback(gui, world, player, this);
	}
	
	
        public ICommandCallback getCallback(int command, int param)
        {
            	if(!callbacks.containsKey(command))
			return null;
                else if(!callbacks.get(command).containsKey(param))
                        return null;
                else 
                    return callbacks.get(command).get(param);
        }
        
	// Setter - Getter
	public void setCallback(int command, int param, ICommandCallback callback){
		if(!callbacks.containsKey(command))
			callbacks.put(command, new HashMap<>());
		
		callbacks.get(command).put(param, callback);
	}
	
	public void setCallback(int[] command, int param, ICommandCallback callback){
		for(int i = 0; i < command.length; ++i){
			if(!callbacks.containsKey(command[i]))
				callbacks.put(command[i], new HashMap<>());
			
			callbacks.get(command[i]).put(param, callback);
		}
	}
	
	public void setEnterCallback(IEnterExitCallback callback){
		enterCallback = callback;
	}
	
	public void setEnterText(String enterText){
		this.enterText = enterText;
	}
        
        public void setObserveText(String observe){
		this.observeText = observe;
	}
	
	public void setExitCallback(IEnterExitCallback callback){
		exitCallback = callback;
	}
	
	public void setExitText(String enterText){
		this.exitText = enterText;
	}
	
	public void setAdjacentLocation(int location, int[] correspondingParams, int[] attributes){
		for(int i = 0; i < correspondingParams.length; ++i){
			if(!adjacentLocations.containsKey(correspondingParams[i]))
				adjacentLocations.put(correspondingParams[i], new ArrayList<Integer>());
			
			if(!adjacentLocations.get(correspondingParams[i]).contains(location))
					adjacentLocations.get(correspondingParams[i]).add(location);
		}
		
		if(!adjacentLocationAttributes.containsKey(location))
			adjacentLocationAttributes.put(location, new ArrayList<Integer>());
		
		for(int j = 0; j < attributes.length; ++j){
			if(!adjacentLocationAttributes.get(location).contains(attributes[j]))
				adjacentLocationAttributes.get(location).add(attributes[j]);
		}
	}
	
        
        public void setBildID(int id) {
           bildID = id;
       }

       public void setSoundID(int id) {
           soundID = id;
       }

       public String getObserveText()
       {
           return observeText;
       }
       public int getSoundID() {
           return this.soundID;
       }

       public int getBildID() {
           return this.bildID;
       }
        
        
	public String getEnterText(){
		return this.enterText;
	}
	
	public String getExitText(){
		return this.exitText;
	}
	
	public ArrayList<Integer> getAdjacentLocations(int param){
		if(adjacentLocations.containsKey(param) && param != -1)
			return adjacentLocations.get(param);
		return new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getAdjacentLocationAttributes(int location){
		if(adjacentLocationAttributes.containsKey(location))
			return adjacentLocations.get(location);
		return new ArrayList<Integer>();
	}
	
	
	// Helper Functions
	public void observe(GUI gui){
		gui.writeln(observeText, 0, 200);
	}
	
	public void observe(GUI gui, String text){
		gui.writeln(text, 0, 200);
	}
}
