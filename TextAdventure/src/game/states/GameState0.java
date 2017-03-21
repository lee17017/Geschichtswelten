package game.states;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import game.Entity;
import game.Event;
import game.GUI;
import game.GameState;
import game.Player;
import world.World;

public class GameState0 extends GameState{

	int monologCount = 0;
	String[] monolog = {
		"Heute ist es endlich soweit.",
		"Ich werde im Castle der Familie Graham meine Untersuchungen beginnen.",
		"Seine Lordschaft stammt bekanntlich aus einer alten, aber einflussreichen Adelsfamilie, "
				+ "die nur aufgrund des Unternehmens von Edward ihren Landsitz erhalten konnte.",
		"Der Fall h�rte sich f�r mich sehr interessant an, "
				+ "weswegen ich mich daf�r entschied undercover als Butler der Grahams eine Weile zu arbeiten.",
		"Nun reise ich mit dem Zug nach Edinburgh um von dort aus mit einem Taxi in die Highlands weiter zu fahren.."
	};
	
	int dialogCount = 0;
	String[] dialog = {
		"�Sie wollen zum Schloss der Grahams?�",
		"�Ganz Richtig�",
		"�Sie beginnen dort also zu arbeiten? Haben sie auch etwas Angst?�",
		"�Wieso denn Angst? Aufgeregt bin ich, weil es mein erster Arbeitstag ist. Ich freue mich sehr auf das Umfeld dort und bin absolut gespannt.�",
		"�Ach, man erz�hlt sich nur, dass es im Castle der Familie Graham spuken soll.",
		"Manchmal h�rt man dort nachts Musik.",
		"Das ist es zumindest, was sich die Leute erz�hlen.�",
		"�Ah ich verstehe. Grund daf�r ist nat�rlich ein Geist.�",
		"Der Taxifahrer schmunzelt.",
		"�Ganz richtig ein Geist. In Schottland spukt es ja �berall nicht wahr?",
		"Allein Edinburgh wird nachts von den unheimlichsten Geistern heimgesucht.�",
		"�Ja das habe ich auch schon geh�rt. Die verspukteste Stadt Europas nicht wahr?�",
		"�Das ist das besondere an Edinburgh.",
		"Am Tage romantisch und in der Nacht gespenstisch.�",
		"Zeit verstreicht...",
		"�Hier ist es, das Schloss der Adelsfamilie Graham. "
				+ "Soll ich ihnen das Gep�ck hinein tragen?�",
		"�Nein danke das schaffe ich schon. "
				+ "Nett von Ihnen. Auf Wiedersehen!�",
		"�Viel Erfolg bei Ihrer Arbeit!�"
		};
	
	public GameState0(World world, Player player, Map<Integer, Entity> entities) {
		super(world, player, entities);
		dialogCount = 0;
	}

	@Override
	public void OnStart(GUI gui) {
                gui.setInputMessage("Press Enter");
		gui.writeln(monolog[0], 50, 0);
		gui.writeln(monolog[1], 50, 500);
		gui.writeln(monolog[2], 50, 500);
		gui.writeln(monolog[3], 50, 500);
		gui.writeln(monolog[4], 50, 500);
	}

	@Override
	public void AfterCommand(GUI gui, int command, int param, boolean successful) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnFinish(GUI gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameState nextState() {
		// TODO Auto-generated method stub
		return new GameState1(world, player, entities);
	}

	@Override
	public void OnEnterPressed(GUI gui) {
		if(!gui.stillWriting() && dialogCount >= dialog.length-1){
			finished = true;
			return;
		}
		
		if(gui.stillWriting() && dialogCount == 0)
			gui.forceWrite(false);
		
		if(!gui.stillWriting() && dialogCount == 0){
			gui.writeln("\n\n", 0, 0);
			gui.writeln("Taxifahrer: ", 0, 0);
			gui.writeln(dialog[dialogCount++], 50, 0);
		}
		
		if(gui.stillWriting() && dialogCount > 0)
			gui.forceWrite(false);
		
		if(!gui.stillWriting() && dialogCount > 0){
			if(dialogCount == 0 || dialogCount == 2 || dialogCount == 4 || dialogCount == 9 || dialogCount == 12)
				gui.writeln("Taxifahrer: ", 0, 0);
			else if(dialogCount == 1 || dialogCount == 3 || dialogCount == 7 || dialogCount == 11)
				gui.writeln("Meine Antwort: ", 0, 0);
			gui.writeln(dialog[dialogCount++], 50, 0);
		}
	}

	@Override
	public void OnEvent(GUI gui, int event, String from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdate(GUI gui, Event event) {
		// TODO Auto-generated method stub
		
	}

}
