package game.states;

import java.util.ArrayList;
import java.util.Map;

import game.Entity;
import game.Event;
import game.Character;
import game.Dialog;
import game.GUI;
import game.Game;
import game.GameState;
import game.Player;
import world.Section;
import world.World;

public class GameState2 extends GameState{
	int scarlett;
	int elliot;
	int dean;
	int viola;
	
	boolean scarlettVerlassen = false;
	boolean violaVerlassen = false;
	boolean elliotVerlassen = false;
	
	int dialogCounter = 0;
	String[] dialog = {
		"Scarlett Graham",
		"„Guten Tag!“",
		"Viola Rose",
		"„Herzlich Willkommen auf Castle Dumour.“"
	};
	
	public GameState2(World world, Player player, Map<Integer, Entity> entities) {
		super(world, player, entities);
	}

	@Override
	public void OnStart(GUI gui) {
		scarlett = createCharacter("Scarlett Graham", "Scarlett", 19);
		elliot = createCharacter("Elliot Graham", "Elliot", 20);
		dean = createCharacter("Dean", "Dean", 21);
		viola = createCharacter("Viola Rose", "Viola", 23);
		
		setEntityPos(scarlett, world, 2, 0, 0);
		setEntityPos(viola, world, 2, 0, 0);
		setEntityPos(elliot, world, 2, 0, 0);
		
		/// Scarletts Dialog
		Dialog scarlettDialog = new Dialog();
		Dialog scarlettDialog2 = new Dialog();
		Dialog scarlettDialog3 = new Dialog();
		Dialog scarlettDialog4 = new Dialog();
		Dialog scarlettDialog5 = new Dialog();
		
		scarlettDialog.characterName = "Scarlett";
		scarlettDialog2.characterName = "Scarlett";
		scarlettDialog3.characterName = "Scarlett";
		scarlettDialog4.characterName = "Scarlett";
		scarlettDialog5.characterName = "Scarlett";
		
		String[] ct = {"„Sie sind der neue Butler! Wie schön, dass sie schon hier angekommen sind.“"};
		String[][] pa = {
				{"„Wie schön, dich kennen zu lernen. Du bist die Tochter des Schlossherren -  sehe ich das richtig?“"},
				{"„Die Freude ist ganz meinerseits. Die Taxifahrt war sehr angenehm. Ich bin sehr erfreut, hier arbeiten zu dürfen. Sie sind Lady Scarlett, richtig?“"}
		};
		
		scarlettDialog.characterText = ct;
		scarlettDialog.possibleAnswers = pa;
		
		
		String[] ct2 = {"„Ganz richtig. Mein Vater ist Edward Graham.“"};
		String[][] pa2 = {
				{"„Wo ist ihr Vater denn gerade?“"},
		};
		
		scarlettDialog2.characterText = ct2;
		scarlettDialog2.possibleAnswers = pa2;
		
		String[] ct3 = {"„Als ob ich das wüsste. Wir haben im Moment ein eher schwieriges Verhältnis. Naja, ist auch nicht so wichtig. Ich hoffe Sie haben einen guten ersten Tag.“"};
		String[][] pa3 = {};
		
		scarlettDialog3.characterText = ct3;
		scarlettDialog3.possibleAnswers = pa3;
		
		String[] ct4 = {"„Sie brauchen doch nicht so förmlich mit mir zu reden. Nennen sie mich einfach Scarlett - nur Scarlett.“"};
		String[][] pa4 = {
				{"„Na gut, nur Scarlett. Ich bin unglaublich froh, dass heute mein erster Arbeitstag in diesem wundervollen Schloss ist.“"},
		};
		
		scarlettDialog4.characterText = ct4;
		scarlettDialog4.possibleAnswers = pa4;
		
		String[] ct5 = {"„Das Schloss ist wundervoll, nicht wahr?“"};
		String[][] pa5 = {
				{"„Es ist reizend.“"},
		};
		
		scarlettDialog5.characterText = ct5;
		scarlettDialog5.possibleAnswers = pa5;
		
		scarlettDialog.outcomes.put(0, scarlettDialog2);
		scarlettDialog.outcomes.put(1, scarlettDialog4);
		scarlettDialog2.outcomes.put(0, scarlettDialog3);
		scarlettDialog4.outcomes.put(0, scarlettDialog5);
			
		scarlettDialog3.setDialogBehaviour(new Dialog.IDialogBehaviour() {
			
			@Override
			public void diag_behave(GUI gui, World world, Player player) {
				setEntityPos(scarlett, world, 6, 0, 0);
				gui.writeln("Scarlett verlässt den Raum.", 0, 500);
				
				scarlettVerlassen = true;
				
				if(scarlettVerlassen && violaVerlassen && elliotVerlassen){
					gui.writeln("Sieh dich um.", 0, 600);
					world.addRoomSectionCallback(2, 0, 0, new Section.ICommandCallback() {
						
						@Override
						public void callback(GUI gui, World world, Player player, int command, int param, int self) {
							if(command == 5)
								gui.writeln("Die Eingangshalle führt zu einer Tür auf der linken Seite oder in den Garten.", 0, 200);
							
							if(command == 0 && param == 30){
								gui.writeln("Sie ist verschlossen.", 0, 200);
							}
							
							if(command == 0 && param == 4){
								player.setState(world, 12, 0, 0, 'n');
								gui.writeln("Sie sind im Garten.", 0, 200);
							}
								
						}
					});
				}
			}
		});
		
		scarlettDialog5.setDialogBehaviour(new Dialog.IDialogBehaviour() {
			
			@Override
			public void diag_behave(GUI gui, World world, Player player) {
				setEntityPos(scarlett, world, 6, 0, 0);
				gui.writeln("Scarlett verlässt den Raum.", 0, 500);
				
				scarlettVerlassen = true;
				
				if(scarlettVerlassen && violaVerlassen && elliotVerlassen){
					gui.writeln("Sieh dich um.", 0, 600);
					world.addRoomSectionCallback(2, 0, 0, new Section.ICommandCallback() {
						
						@Override
						public void callback(GUI gui, World world, Player player, int command, int param, int self) {
							if(command == 5)
								gui.writeln("Die Eingangshalle führt zu einer Tür auf der linken Seite oder in den Garten.", 0, 200);
							
							if(command == 0 && param == 30){
								gui.writeln("Sie ist verschlossen.", 0, 200);
							}
							
							if(command == 0 && param == 4){
								player.setState(world, 12, 0, 0, 'n');
								gui.writeln("Sie sind im Garten.", 0, 200);
							}
								
						}
					});
				}
			}
		});
		
		
		/// Elliots Dialog
		Dialog d1 = new Dialog();
		Dialog d2 = new Dialog();
		Dialog d3 = new Dialog();
		Dialog d4 = new Dialog();
		
		d1.characterName = "Elliot";
		d2.characterName = "Elliot";
		d3.characterName = "Elliot";
		d4.characterName = "Elliot";
		
		String[] cte = {"„Hallo, mein Name ist Elliot! Ich bin der Bruder des Schlossherren. Schön Sie kennenzulernen.“"};
		String[][] pae = {
				{"„Die Freude ist ganz meinerseits! Ich bin sehr gespannt auf die Arbeit hier.“"},
		};
		
		d1.characterText = cte;
		d1.possibleAnswers = pae;
		
		
		String[] cte2 = {"„Ich hoffe, Sie haben auf Castle Darkmere, dem Schloss der Familie Graham, viel Spaß! "
				+ "Vielleicht können wir zusammen ja mal einen Whiskey trinken. Mögen Sie Schottischen Whiskey?“"};
		String[][] pae2 = {
				{"„Absolut! Gerne würde ich mit Ihnen an einem Abend gemeinsam einen Whiskey trinken.“"},
				{"„Mit Whiskey habe ich noch keine großen Erfahrungen gemacht.“"},
		};
		
		d2.characterText = cte2;
		d2.possibleAnswers = pae2;
		
		
		String[] cte3 = {"„Das klingt wundervoll. Ein neuer Whiskeykenner auf unserem Schloss. "
				+ "Ich freue mich, Sie später zu sehen, dann können wir gemeinsam ja mal einen Glenmorangie trinken. Bis dann!“"};
		String[][] pae3 = {};
				
		d3.characterText = cte3;
		d3.possibleAnswers = pae3;
		
		
		String[] cte4 = {"„Das müssen wir auf alle Fälle ändern! Wir sehen uns später.“"};
		String[][] pae4 = {};
		
		d4.characterText = cte4;
		d4.possibleAnswers = pae4;
		
		d1.outcomes.put(0, d2);
		d2.outcomes.put(0, d3);
		d2.outcomes.put(1, d4);
		
		d3.setDialogBehaviour(new Dialog.IDialogBehaviour() {
			
			@Override
			public void diag_behave(GUI gui, World world, Player player) {
				setEntityPos(elliot, world, 6, 0, 0);
				gui.writeln("Elliot verlässt den Raum.", 0, 500);
				
				Character scaEnt = (Character) getEntity(scarlett);
				Character vioEnt = (Character) getEntity(viola);
				
				elliotVerlassen = true;
				
				if(scarlettVerlassen && violaVerlassen && elliotVerlassen){
					gui.writeln("Sieh dich um.", 0, 600);
					world.addRoomSectionCallback(2, 0, 0, new Section.ICommandCallback() {
						
						@Override
						public void callback(GUI gui, World world, Player player, int command, int param, int self) {
							if(command == 5)
								gui.writeln("Die Eingangshalle führt zu einer Tür auf der linken Seite oder in den Garten.", 0, 200);
							
							if(command == 0 && param == 30){
								gui.writeln("Sie ist verschlossen.", 0, 200);
							}
							
							if(command == 0 && param == 4){
								player.setState(world, 12, 0, 0, 'n');
								gui.writeln("Sie sind im Garten.", 0, 200);
							}
								
						}
					});
				}
			}
		});
		
		d4.setDialogBehaviour(new Dialog.IDialogBehaviour() {
			
			@Override
			public void diag_behave(GUI gui, World world, Player player) {
				setEntityPos(elliot, world, 6, 0, 0);
				gui.writeln("Elliot verlässt den Raum.", 0, 500);
				
				elliotVerlassen = true;
				
				if(scarlettVerlassen && violaVerlassen && elliotVerlassen){
					gui.writeln("Sieh dich um.", 0, 600);
					world.addRoomSectionCallback(2, 0, 0, new Section.ICommandCallback() {
						
						@Override
						public void callback(GUI gui, World world, Player player, int command, int param, int self) {
							if(command == 5)
								gui.writeln("Die Eingangshalle führt zu einer Tür auf der linken Seite oder in den Garten.", 0, 200);
							
							if(command == 0 && param == 30){
								gui.writeln("Sie ist verschlossen.", 0, 200);
							}
							
							if(command == 0 && param == 4){
								player.setState(world, 12, 0, 0, 'n');
								gui.writeln("Sie sind im Garten.", 0, 200);
							}
								
						}
					});
				}
			}
		});
		
		
		
		/// Violas Dialog
		Dialog violaDialog = new Dialog();
		Dialog violaDialog2 = new Dialog();
		Dialog violaDialog3 = new Dialog();
		Dialog violaDialog4 = new Dialog();
		Dialog violaDialog5 = new Dialog();
		
		violaDialog.characterName = "Viola";
		violaDialog2.characterName = "Viola";
		violaDialog3.characterName = "Viola";
		violaDialog4.characterName = "Viola";
		violaDialog5.characterName = "Viola";
		
		String[] ctv = {"„ Nun möchte ich mich auch bei Ihnen vorstellen. Ich bin Viola Rose. "
				+ "Die Verlobte des Schlossherren. "
				+ "Ich hoffe, wir werden in Zukunft gut miteinander auskommen. "
				+ "Einen Butler haben wir sehr dringend gebraucht und ich hoffe sehr, Sie werden uns nicht enttäuschen.“"};
		String[][] pav = {
				{"„Das hoffe ich auch, gnädige Frau. Wie lange sind sie schon mit Edward Graham verlobt?“"},
				/*{"„Sie zu enttäuschen wird sich hoffentlich vermeiden lassen. Ich werde mich gleich an die Arbeit machen.“"}*/
		};
		
		violaDialog.characterText = ctv;
		violaDialog.possibleAnswers = pav;
		
		
		String[] ctv2 = {"„Seit einem halben Jahr. Hoffentlich werden wir bald heiraten. Er ist ein wundervoller Mann.“"};
		String[][] pav2 = {
				{"„Das glaube ich Ihnen gerne. Wie haben Sie Ihren Verlobten kennen gelernt?“"},
		};
		
		violaDialog2.characterText = ctv2;
		violaDialog2.possibleAnswers = pav2;
		
		String[] ctv3 = {"„Über die Arbeit. Er ist Geschäftsführer des Mutterkonzerns der Firma, in der ich arbeite. "
				+ "Apropos Arbeit, ich denke, Sie sollten sich langsam auf Ihre eigene Arbeit konzentrieren.“"};
		String[][] pav3 = {
				{"„Es war sehr nett mit Ihnen zu plaudern, gnädige Frau. Ich werde mich umsehen und mich gleich an die Arbeit machen.“"}
		};
		
		violaDialog3.characterText = ctv3;
		violaDialog3.possibleAnswers = pav3;
		
		violaDialog3.setDialogBehaviour(new Dialog.IDialogBehaviour() {
			
			@Override
			public void diag_behave(GUI gui, World world, Player player) {
				setEntityPos(viola, world, 6, 0, 0);
				gui.writeln("Viola verlässt den Raum.", 0, 500);
				
				violaVerlassen = true;
				
				if(scarlettVerlassen && violaVerlassen && elliotVerlassen){
					gui.writeln("Sieh dich um.", 0, 600);
					world.addRoomSectionCallback(2, 0, 0, new Section.ICommandCallback() {
						
						@Override
						public void callback(GUI gui, World world, Player player, int command, int param, int self) {
							if(command == 5)
								gui.writeln("Die Eingangshalle führt zu einer Tür auf der linken Seite oder in den Garten.", 0, 200);
							
							if(command == 0 && param == 30){
								gui.writeln("Sie ist verschlossen.", 0, 200);
							}
							
							if(command == 0 && param == 4){
								player.setState(world, 12, 0, 0, 'n');
								gui.writeln("Sie sind im Garten.", 0, 200);
							}
								
						}
					});
				}
			}
		});
		
		violaDialog.outcomes.put(0, violaDialog2);
		violaDialog2.outcomes.put(0, violaDialog3);
		//violaDialog4.outcomes.put(0, violaDialog5);
			
		setCharacterDialog(scarlett, scarlettDialog);
		setCharacterDialog(viola, violaDialog);
		setCharacterDialog(elliot, d1);
		
		gui.writeln("Die Tochter des Schlossherren Scarlett Graham, seine Verlobte Viola Graham und sein Bruder Elliot Graham begrüßen Sie.", 0, 400);
		gui.enableWriting(false);
	}

	@Override
	public void OnEnterPressed(GUI gui) {
		
		if(gui.stillWriting()){
			gui.forceWrite(false);
		}
		else if(!gui.stillWriting() && dialogCounter < 4){
			gui.writeln(dialog[dialogCounter++]+": ", 0, 1000);
			gui.writeln(dialog[dialogCounter++]+": ", 30, 300);
		}
		if(dialogCounter >= 4){
			gui.enableWriting(true);
		}
		
	}

	@Override
	public void AfterCommand(GUI gui, int command, int param, boolean successful) {

	}

	@Override
	public void OnFinish(GUI gui) {
		
	}

	@Override
	public GameState nextState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void OnEvent(GUI gui, int event, String from) {

	}

	@Override
	public void OnUpdate(GUI gui, Event event) {
		// TODO Auto-generated method stub
		
	}

}
