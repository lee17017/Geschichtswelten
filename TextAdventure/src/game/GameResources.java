package game;

import java.util.HashMap;

import game.Dialog.DialogText;
import world.World;
import world.Location;
public class GameResources {
	
	///--- Default Outputs
	public static String defaultUnknownCommandText = "Das verstehe ich nicht.";
	public static String defaultNotAllowedText = "Das kannst du nicht tun.";
	public static String defaultHereNotAllowedText = "Das kannst du hier nicht tun.";
	
	public static String defaultMultiplePossibilitiesText = "Drücke dich genauer aus.";
	public static String defaultYouAreInAConversationText = "Du befindest dich in einer Konversation.";
	
	public static String defaultNoObjectText = "Du musst ein Objekt angeben.";
	public static String defaultNoLocationText = "Du musst ein Ziel angeben.";
	public static String defaultInvalidDiscussionPartnerText = "Du musst eine Person angeben.";
	public static String defaultInvalidNumberInDiscussionText = "Du musst eine gültige Zahl angeben.";
	
	public static String defaultTooFarAwayText = "Du bist zu weit entfernt.";
	public static String defaultDiscussionPartnerNotHereText = "%s ist nicht in deiner Nähe.";
	
	public static String defaultInvalidAttributeText = "Das Attribut stimmt nicht.";
	public static String defaultInvalidLocationText = "Das ist kein gültiges Ziel.";
	
	public static String defaultPressEnterText = "Enter";
	
	
	
	///--- Game Texts
	public static String prolog_1_header = "GEDANKEN";
	
	public static String[] prolog_1 = {
		"Heute ist es endlich so weit. "
		+ "Ich werde im Schloss der Familie Graham meine Untersuchungen beginnen. "
		+ "Mein Auftrag ist es heraus zu finden, ob Lord Edward Graham sich der Steuerhinterziehung schuldig gemacht hat oder nicht.",
		
		"Seine Lordschaft stammt aus einer alten, einflussreichen Adelsfamilie, die jedoch eine finanziell recht schwierige Zeit hinter sich hat. "
		+ "Dies ging so weit, dass die Familie vor Kurzem gezwungen war, ihr ganzes Personal zu feuern und ihren Landsitz nur aufgrund von Edwards Unternehmen überhaupt erhalten konnte.",
		
		"Die Familienfinanzen scheinen sich jedoch kürzlich wieder gebessert zu haben, was erstens zu den Anschuldigungen gegenüber Edward Graham geführt hat "
		+ "und zweitens mir die Chance gibt mich als neuer Butler der Grahams in den Familiensitz einzuschleichen und dort verdeckte Ermittlungen durch zu führen.",
		
		"Ich bin mit dem Zug nach Edinburgh gefahren und warte jetzt auf mein Taxi, das mich in die Highlands zum Anwesen der Grahams bringen wird.\n"
	};
	public static HashMap<Integer, Game.IInputCallback> prolog_1_callback_map;
	
	public static String[] prolog_2 = {
		"„Sie wollen also zum Schloss der Grahams?“",
		"„Ganz richtig.“",
		"„Sie werden dort in Zukunft arbeiten? Haben sie nicht etwas Angst?“",
		"„Na ja, ich bin schon etwas aufgeregt. Es ist immerhin mein erster Arbeitstag. Ich bin gespannt darauf wie die Familie Graham so ist. Aber warum sollte ich denn Angst haben?“",
		"„Ach, nichts weiter. Es gibt halt Gerüchte.“",
		"„Gerüchte?“",
		"„Im Schloss der Familie Graham soll es spuken. Anscheinend hört man dort nachts manchmal ganz schaurige Musik. Das ist es zumindest, was sich die Leute erzählen.“",
		"„Ah, ich verstehe. Es gibt also unheimliche Geräusche. Und der Grund dafür muss natürlich ein Geist sein?“",
		"„Ganz richtig ein Geist. In Schottland spukt es ja überall nicht wahr? Edinburgh wird bereits nachts von den unheimlichsten Geistern heimgesucht. Wie soll es denn erst draußen in den Highlands sein?“",
		"„Stimmt, davon habe ich schon gehört. Edinburgh ist weltweit für seine Geister bekannt, nicht wahr?“",
		"„Ja, das ist das besondere an der Stadt. Am Tage romantisch und in der Nacht gespenstisch.“\n",
		"Zeit verstreicht...\n",
		"„So, wir sind da. Das Schloss der Familie Graham. Soll ich Ihnen mit ihrem Gepäck helfen?“",
		"„Nein, das schaffe ich schon. Aber vielen Dank für das Angebot. Auf Wiedersehen!“",
		"„Viel Erfolg bei Ihrer neuen Arbeit.“\n",
		""
	};
	public static String[] prolog_2_prefixes = {
		"Taxifahrer erzählt:\n",
		"Meine Antwort:\n",
		"Taxifahrer erzählt:\n",
		"Meine Antwort:\n",
		"Taxifahrer erzählt:\n",
		"Meine Antwort:\n",
		"Taxifahrer erzählt:\n",
		"Meine Antwort:\n",
		"Taxifahrer erzählt:\n",
		"Meine Antwort:\n",
		"Taxifahrer erzählt:\n",
		"",
		"Taxifahrer erzählt:\n",
		"Meine Antwort:\n",
		"Taxifahrer erzählt:\n",
		"Du bezahlst den Taxifahrer und steigst mit deinem Gepäck aus dem Auto aus.\n"
		+ "Du stehst nun vor dem Anwesen der Familie Graham.\n"
		+ "Du solltest vermutlich versuchen den Weg ins Schloss zu finden, um dich der Familie vorzustellen und mit deiner Arbeit anzufangen.\n"
		+ "\nBitte Befehl eingeben:"
	};
	public static HashMap<Integer, Game.IInputCallback> prolog_2_callback_map;
	
	
	
	///--- Dialogs
	public static Dialog torDialog = new Dialog();
	public static Dialog willkommensDialog = new Dialog();
	public static Dialog viola1Dialog = new Dialog();
	public static Dialog elliot1Dialog = new Dialog();
	public static Dialog dean1Dialog = new Dialog();
	
	
	
	///--- Init
	public static void init(){
		/// Callback Maps
		prolog_1_callback_map = new HashMap<Integer, Game.IInputCallback>();
		prolog_2_callback_map = new HashMap<Integer, Game.IInputCallback>();
		prolog_2_callback_map.put(3, new Game.IInputCallback() {
			
			@Override
			public boolean callback(GUI gui, World world, Player player, String input) {
				gui.setBg(8);
				return true;
			}
		});
		
		
		
		/// Dialog Tor
		int tor_1 = torDialog.createDialogText("???");
		int tor_2 = torDialog.createDialogText("Du");
		int tor_3 = torDialog.createDialogText("Du");
		int tor_4 = torDialog.createDialogText("???");
		
                //Viola 1 Dialog
		int viola_1 = viola1Dialog.createDialogText("Viola");
		viola1Dialog.setCharacterText(viola_1, -1, "Scarlett wird sicher bald mit ihrem Vater zurückkehren.");
                
		torDialog.setCharacterText(tor_1, tor_2, "Hallo?");
		torDialog.setCharacterText(tor_2, tor_3, "Guten Tag, ich bin... ");
		
		torDialog.setDialogTextCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				
				gui.writeln("Bitte gib deinen Namen ein.", 0, 200);
				gui.setInputMessage("");
				gui.enableWriting(true);
				
				Game.Get().addInputCallback(new Game.IInputCallback() {
					
					@Override
					public boolean callback(GUI gui, World world, Player player, String input) {
						if(input.equals(""))
							input = "Mac NoName";
						player.setName(input);
						
						torDialog.setCharacterText(tor_4, -1, "Oh, Willkommen Mr. "+input+". Ich öffne ihnen gleich das Tor. Die Haustür ist nicht abgeschlossen, treten Sie also einfach ein. Ich erwarte Sie in der Eingangshalle.");
						torDialog.setCharacterText(tor_3, tor_4, "Ich bin "+input+", der neue Butler.");
						Game.Get().addEvent(1, new String[]{});
						
						gui.setInputMessage(GameResources.defaultPressEnterText);
						gui.enableWriting(false);
						
						return true;
					}
				});
			}
		}, tor_2);
		
		torDialog.setEndCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				gui.writeln("Kaum ist die Stimme aus der Gegensprechanlage verstummt, öffnet sich auch schon das Tor vor dir.", 0, 1000);
				Game.Get().getCharacterByPreName("viola").setDialog(GameResources.viola1Dialog);
                                System.out.println("asdf");
			}
		});
		
		
		
		/// Dialog Willkommen
		int willkommen_1 = willkommensDialog.createDialogText("???");
		int willkommen_2 = willkommensDialog.createDialogText("???");
		int willkommen_3 = willkommensDialog.createDialogText("Viola");
		
		willkommensDialog.setCharacterText(willkommen_1, willkommen_2, "Noch einmal herzlich Willkommen auf Schloss Darkmere. Ich bin Viola Rose, die Verlobte von Edward Graham.");
		willkommensDialog.setCharacterText(willkommen_2, willkommen_3, "Wenn wir schon von meinem verehrten Herrn Vater reden, darf ich denn erfahren wo er sich aufhält? "
				+ "Sollte es nicht seine Aufgabe sein, den neuen Butler zu empfangen?");
		willkommensDialog.setCharacterText(willkommen_3, -1, "Er ist sicher noch in seinem Arbeitszimmer und arbeitet, Scarlett. Er hat die Klingel wahrscheinlich nicht gehört. Wärst du so nett ihn zu holen?");
		
		willkommensDialog.setDialogTextCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				gui.writeln("Die zweite Frau, deutlich jünger und mit erstaunlich rotem Haar, bricht ihr Gespräch mit dem Mann ab und wendet sich ebenfalls dir zu, um Viola ins Wort zu fallen.", 0, 800);
				//Game.Get().addEvent(1, new String[]{});
			}
		}, willkommen_1);
		
		willkommensDialog.setDialogTextCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				gui.writeln("Scarlett nickt Viola höflich zu und verlässt, augenscheinlich leicht genervt, die Eingangshalle über eine der Treppen.", 0, 800);
				//Game.Get().addEvent(1, new String[]{});
			}
		}, willkommen_3);
		
		
		
		
		
		
		
		//Elliot 1 Dialog
		int elliot_1 = elliot1Dialog.createDialogText("???");
		int elliot_2 = elliot1Dialog.createDialogText("???");
		int elliot_3 = elliot1Dialog.createDialogText("Viola");
		
		elliot1Dialog.setCharacterText(elliot_1, elliot_2, "Guten Tag. Ah, ich wurde ja noch gar nicht vorgestellt. Ich bin Elliot Graham, Edwards Bruder. "
				+ "Damit hast du jetzt schon Bekanntschaft mit fast allen Bewohnern des Schlosses gemacht. ");
		elliot1Dialog.setCharacterText(elliot_2, elliot_3, "… Huh? Wen du noch nicht kennst? Das wäre wohl Dean, der Gärtner. Du solltest ihn draußen im Garten bei seinen Pflanzen finden.");
		
		elliot1Dialog.setCharacterText(elliot_3, -1, "Hmm… Es scheint doch länger zu dauern bis Edward zu uns stößt, es wäre also wahrscheinlich eine gute Idee, wenn du dich schon mal Dean vorstellst. "
				+ "Ich geh mal nachschauen, wo die beide bleiben.");
		
		elliot1Dialog.setDialogTextCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				gui.writeln("Viola verlässt, wie auch schon Scarlett zuvor, den Raum über eine der Treppen. "
						+ "Als du dich wieder umdrehst siehst du auch Elliot durch eine der Türen im Innern des Hauses verschwinden.", 0, 800);
		
                        Location.getLocation(8).setCallback(1, 11, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
               gui.writeln("Viola ist nicht in deiner Nähe.", 0, 200);
            }
        });

        Location.getLocation(8).setCallback(1, 10, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Elliot ist nicht in deiner Nähe.", 0, 200);
            }
        });
        Location.getLocation(8).setCallback(1, 28, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Elliot ist nicht in deiner Nähe.", 0, 200);
            }
        });
          Location.getLocation(8).setCallback(0, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du gehst durch die Gartentür.", 0, 200);
                player.setLocation(gui, world, 9);
            }
        });
               Location.getLocation(8).setCallback(0, 13, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, 9);
            }
        });
                        }
                        
                        
		}, elliot_3);
		
		
		
		//Dean 1 Dialog
		int dean_1 = dean1Dialog.createDialogText("Du");
		int dean_2 = dean1Dialog.createDialogText("Du");
		int dean_3 = dean1Dialog.createDialogText("???");
		
		dean1Dialog.setCharacterText(dean_1, dean_2, "Entschuldigen Sie?");
		
		dean1Dialog.setDialogTextCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				gui.writeln("Der Mann zuckt erschrocken zusammen und dreht sich zu dir um. Er scheint ziemlich geschockt.", 0, 800);
			}
		}, dean_1);
		
		dean1Dialog.setCharacterText(dean_2, dean_3, "Oh, es tut mir Leid. Ich wollte Sie nicht erschrecken. Ist alles in Ordnung? Sind Sie Dean? Waren Sie es, der geschrien hat?");
		
		dean1Dialog.setDialogTextCallback(new Dialog.IDialogCallback() {
			
			@Override
			public void callback(GUI gui, World world, Player player, DialogText dialogText) {
				
				Game.Get().addInputCallback(new Game.IInputCallback() {
					@Override
					public boolean callback(GUI gui, World world, Player player, String input) {
						if(!input.equals(""))
							return false;
						
						gui.writeln("???: ", 0, 200);
						gui.write("J-… ", 25, 400);
						gui.write("ja, ", 25, 400);
						gui.write("i- ", 25, 400);
						gui.write("ich ", 25, 400);
						gui.write("bin ", 25, 400);
						gui.write("D-… ", 25, 400);
						gui.write("Dean. ", 25, 400);
						gui.write("Es… ", 25, 400);
						gui.write("Ich… ", 25, 400);
						gui.write("D-… ", 25, 400);
						gui.write("Da!", 25, 400);
						
						gui.writeln("Dean dreht sich halb herum und zeigt auf etwas hinter sich. "
								+ "Da Dean keine Anstalten macht sich zu bewegen, musst du dich halb um ihn herumbewegen, bis du schließlich siehst, was er dir zeigen wollte. "
								+ "In einer Ecke des Labyrinths liegt zusammengesunken Edward Graham.", 0, 800);
						
						return true;
					}
				});
				
			}
		}, dean_2);
	}
	
	
	
}
