package game;

import java.util.ArrayList;
import java.util.HashMap;

import game.states.GameState0;
import game.states.GameState1;
import world.World;

public class Game {
	private boolean isRunning;
	
	private GUI gui;
	public GameState currentState;
	
	private ArrayList<Event> events;
	
	
	private Game(){
		events = new ArrayList<Event>();
		isRunning = false;
	}
	
	/*
	 * GAME LOGIC
	 */
	public void Start(){
		currentState = new GameState0(new World(), new Player(), new HashMap<Integer, Entity>());
		if(currentState == null)
			return;
		
		isRunning = true;
		Run();
	}
	
	public void Run(){
		currentState.OnStart(gui);
		while(isRunning){
			if(!events.isEmpty())
				currentState.OnUpdate(gui, events.remove(events.size()-1));
			
			if(currentState.isFinished()){
				currentState.OnFinish(gui);
				currentState = currentState.nextState();
				if(currentState == null) isRunning = false;
			}
		}
	}
	
	/*
	 * 0: gehe
	 * 1: reden
	 * 2: nehmen
	 * 3: öffnen
	 * 4: benutzen
	 * 5: betrachten
	 * 6: bewegen
	 * 10: dialog
	 */
	/*
	public void OnCommand(int command, int param){
		currentState.AfterCommand(gui, command, param, 
				currentState.OnCommand(gui, command, param));
		
		if(currentState.isFinished()){
			currentState.OnFinish(gui);
			currentState = currentState.nextState();
			if(currentState != null) currentState.OnStart(gui);
		}
	}
	*/
	public void OnInput(String input){
		if(gui.stillWriting())
			return;
		
		int command;
		int param;
		
		if(currentState.player.isInDialog()){
			int number;
			try{
				number = Integer.parseInt(input);
			} catch(NumberFormatException e){
				return;
			}
			command = 10;
			param = number;
		}
		else {
			command = Parser.command(input);
			if(command == -1)
				return;
			param = Parser.param(input);
		}
		
		currentState.AfterCommand(gui, command, param, 
				currentState.OnCommand(gui, command, param)
				);
		
		if(currentState.isFinished()){
			currentState.OnFinish(gui);
			currentState = currentState.nextState();
			if(currentState != null) currentState.OnStart(gui);
		}
	}
	
	public void OnEnterPressed(){
		currentState.OnEnterPressed(gui);
		if(currentState.isFinished()){
			currentState.OnFinish(gui);
			currentState = currentState.nextState();
			if(currentState != null) currentState.OnStart(gui);
		}
	}
	
	
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
