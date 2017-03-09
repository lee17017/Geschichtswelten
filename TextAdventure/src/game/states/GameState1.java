package game.states;

import java.util.ArrayList;
import java.util.Map;

import game.Entity;
import game.Event;
import game.GUI;
import game.GameState;
import game.Player;
import world.Section;
import world.World;

public class GameState1 extends GameState{
	
	public GameState1(World world, Player player, Map<Integer, Entity> entities){
		super(world, player, entities);
	}

	@Override
	public void OnStart(GUI gui) {
		gui.enableWriting(true);
		
		// create world
		int anwesen = world.createRoom("anlage", 28);				//0
		int eingangsflur = world.createRoom("eingangsflur", 27);	//1
		int eingangshalle = world.createRoom("eingangshalle", 6);	//2
		int bibliothek = world.createRoom("bibliothek", 7);			//3
		int speisesaal = world.createRoom("speisesaal", 13);		//4
		int galerie = world.createRoom("galerie", 26);				//5
		int musikzimmer = world.createRoom("musikzimmer", 8);		//6
		int kueche = world.createRoom("kueche", 12);				//7
		int weinkeller = world.createRoom("weinkeller", 11);		//8
		int billard = world.createRoom("billardzimmer", 9);			//9
		int lagerraum = world.createRoom("lagerraum", 10);			//10
		int flur = world.createRoom("flur", 27);					//11
		int garten = world.createRoom("garten", 4);					//12
		int labyrinth = addLabyrinth();								//13
		
		world.addRoomSection(anwesen, 0, 0);
		world.addRoomSection(anwesen, 0, 1);
		world.addRoomSection(anwesen, 0, 2);
		world.addRoomSection(eingangsflur, 0, 0);
		world.addRoomSection(eingangsflur, 0, 1);
		world.addRoomSection(eingangshalle, 0, 0);
		world.addRoomSection(bibliothek, 0, 0);
		world.addRoomSection(speisesaal, 0, 0);
		world.addRoomSection(galerie, 0, 0);
		world.addRoomSection(musikzimmer, 0, 0);
		world.addRoomSection(kueche, 0, 0);
		world.addRoomSection(weinkeller, 0, 0);
		world.addRoomSection(billard, 0, 0);
		world.addRoomSection(lagerraum, 0, 0);
		world.addRoomSection(flur, 0, 0);
		world.addRoomSection(garten, 0, 0);
		world.addRoomSection(garten, -1, 0);
       
		//erstelle amulett
		int amulett = createEntity("Amulett", "Amulett", 33);
		Entity amulettEnt = getEntity(amulett);
		amulettEnt.hidden = true;
		
		amulettEnt.updateBehaviour(new Entity.IBehaviour() {
			
			@Override
			public void behave(GUI gui, World world, Player player, int param) {
				gui.writeln("Sie klappen das Amulett auf und auf der linken und rechten Seite sind zwei Frauen mit feuerrotem Haar zu sehen.", 0, 200);
			}
		}, 3);
		
		//garten
	    world.addRoomSectionCallback(garten, 0, 0,
	    		new Section.ICommandCallback() {
		           @Override
		           public void callback(GUI gui, World world, Player player, int command, int param, int self) {
		               if (command == 5) {
		                   gui.writeln("Sie sehen einen wundervollen britischen Garten. Auf der linken Seite befindet sich ein Brunnen. Vor einer Hecke steht ein Mann mit einer Gartenschere. Auf der rechten Seite ist ein Heckenlabyrinth.", 0, 200);
		               }
		               else if (command == 0 && param == 5) {
		                   gui.writeln("Sie sind im Labyrinth.", 0, 200);
		                   player.setState(world, 12, 5, 4, 'n');
		               }
		               else if (command == 0 && param == 32) {
		                   player.goLeft(world);
		                   gui.writeln("Sie stehen direkt vor einem mit Efeu bewachsenen Brunnen. Er besitzt eine Kurbel, um einen Eimer hoch zu ziehen.", 0, 200);
		               }
		           }
	    		});
	       
	    world.addRoomSectionCallback(garten, -1, 0,
	            new Section.ICommandCallback() {
			           @Override
			           public void callback(GUI gui, World world, Player player, int command, int param, int self) {
			               if (command == 5) {
			                   gui.writeln("Sie stehen direkt vor einem mit Efeu bewachsenen Brunnen. Er besitzt eine Kurbel, um einen Eimer hoch zu ziehen.", 0, 200);
			               }
			               else if (command == 0 && param == 4) {
			            	   gui.writeln("Sie stehen wieder im Garten.", 0, 200);
			            	   amulettEnt.hidden = true;
			                   player.goRight(world);
			               }
			               if(command == 2){
			            	   //amulettEnt.hidden = false;
			            	   gui.writeln("Der Eimer ist mit Wasser gefüllt. Etwas innerhalb des Eimers glitzert in der Sonne.", 0, 200);
			            	   gui.writeln("Es ist ein Amulett. Es sieht alt und angerostet aus. Anscheinend kann man das Amulett öffnen.", 0, 400);
			               }
			           }
	       		});
		
		
		//set some section callbacks..
		world.addRoomSectionCallback(anwesen, 0, 0,
				new Section.ICommandCallback() {
					@Override
					public void callback(GUI gui, World world, Player player, int command, int param, int self) {
						if(command == 5){
							gui.writeln("Vor Ihnen ist ein großes Anwesen. Eine breite Zufahrt führt zu dem betagten Backstein Gebäude.", 0, 200);
						}
						
						else if(command == 0 && param == self){
							player.goForward(world);
							gui.writeln("Sie stehen direkt vor dem Schloss.", 0, 200);
						}
						
					}
				});
		
		world.addRoomSectionCallback(anwesen, 0, 1, 
				new Section.ICommandCallback() {
					
					@Override
					public void callback(GUI gui, World world, Player player, int command, int param, int self) {
						if(command == 5){
							gui.writeln("Sie sehen ein altes Gebäude, mit Efeu bewachsenen Mauern. Kleine Türme zieren das Gebäude. "
									+ "Es scheint sehr alt zu sein. Direkt vor ihnen führt ein Treppe zur Tür.", 0, 200);
						}
						
						else if(command == 0 && param == self){
							gui.writeln("Sie stehen bereits vor dem Schloss.", 0, 200);
						}
						
						else if(command == 0 && (param == 30 || param == 31)){
							player.goForward(world);
							gui.writeln("Sie stehen vor der Tür.", 0, 200);
						}
						
					}
				});
		
		world.addRoomSectionCallback(anwesen, 0, 2, 
				new Section.ICommandCallback() {
					
					@Override
					public void callback(GUI gui, World world, Player player, int command, int param, int self) {
						if(command == 5){
							gui.writeln("Sie sehen eine große Eingangstür.", 0, 200);
						}
						
						if(command == 0 && (param == 30 || param == 31))
							gui.writeln("Sie stehen bereits vor der Tür.", 0, 200);
					}
				});
		
		world.addRoomSectionCallback(eingangsflur, 0, 0,
				new Section.ICommandCallback() {
					
					@Override
					public void callback(GUI gui, World world, Player player, int command, int param, int self) {
						if(command == 5)
							gui.writeln("Sie sehen einen Flur, der zu einer Tür führt.", 0, 200);
						
						if(command == 0 && param == 30){
							player.goForward(world);
							gui.writeln("Sie stehen direkt vor der Tür.", 0, 200);
						}
					}
				});
		
		world.addRoomSectionCallback(eingangshalle, 0, 0,
				new Section.ICommandCallback() {
					
					@Override
					public void callback(GUI gui, World world, Player player, int command, int param, int self) {
						if(command == 5)
							gui.writeln("Sie sehen zwei Türen.", 0, 200);
						
						if(command == 0 && param == 30){
							gui.writeln("Sie sollten sich mit den Bewohnern des Schlosses unterhalten.", 0, 200);
						}
					}
				});
		
		
		// set player
		player.setState(world, anwesen, 0, 0, 'n');
		
		// create objects
		createDoor(anwesen, 0, 2, "Sie stehen im Flur des Herrenhauses.", eingangsflur, 0, 0, "Sie stehen draußen vor dem Schloss.", 'n');
		createDoor(eingangsflur, 0, 1, "Sie stehen in der Eingangshalle.", eingangshalle, 0, 0, "Sie stehen im Flur des Herrenhauses.", 'n');
	}

	@Override
	public void AfterCommand(GUI gui, int command, int param, boolean successful) {
		if(world.getRoomParamId(player.getRoom()) == 6)
			finished = true;
	}

	@Override
	public void OnFinish(GUI gui) {
		
	}
	
	@Override
	public GameState nextState() {
		return new GameState2(world, player, entities);
	}

	@Override
	public void OnEnterPressed(GUI gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnEvent(GUI gui, int event, String from) {
		// TODO Auto-generated method stub
		
	}
	
	private int addLabyrinth()
    {
        
        int labyrinth = world.createRoom("labyrinth", 5);
        world.addRoomSection(labyrinth, 5, 1);
        world.addRoomSection(labyrinth, 5, 2);
        world.addRoomSection(labyrinth, 5, 3);
        world.addRoomSection(labyrinth, 5, 4);
        world.addRoomSection(labyrinth, 5, 5);
        world.addRoomSection(labyrinth, 5, 6);
        world.addRoomSection(labyrinth, 4, 6);
        world.addRoomSection(labyrinth, 3, 6);
        world.addRoomSection(labyrinth, 3, 7);

        world.addRoomSection(labyrinth, 4, 3);
        world.addRoomSection(labyrinth, 3, 3);
        world.addRoomSection(labyrinth, 3, 2);
        world.addRoomSection(labyrinth, 2, 2);
        world.addRoomSection(labyrinth, 1, 2);
        world.addRoomSection(labyrinth, 1, 1);

        world.addRoomSection(labyrinth, 3, 4);
        world.addRoomSection(labyrinth, 2, 4);
        world.addRoomSection(labyrinth, 1, 4);
        world.addRoomSection(labyrinth, 1, 5);
        world.addRoomSection(labyrinth, 1, 6);
        
        world.addRoomSection(labyrinth, 0, 6);
        world.addRoomSection(labyrinth, 0, 7);
        world.addRoomSection(labyrinth, 0, 8);
        world.addRoomSection(labyrinth, 1, 8);
        world.addRoomSection(labyrinth, 1, 9);
        world.addRoomSection(labyrinth, 2, 9);
        world.addRoomSection(labyrinth, 3, 9);
        world.addRoomSection(labyrinth, 4, 9);
        world.addRoomSection(labyrinth, 5, 9);
        world.addRoomSection(labyrinth, 5, 8);
        
        world.addRoomSection(labyrinth, 5, 10);
        world.addRoomSection(labyrinth, 5, 11);
        world.addRoomSection(labyrinth, 4, 11);
        
        


        world.addRoomSectionCallback(labyrinth, 3, 7,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie befinden sich in einer Sackgasse, sie können nach unten gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 3, 6,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 4, 6,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });

        world.addRoomSectionCallback(labyrinth, 5, 6,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach unten gehen.", 0, 200);
                }
            }
        });

        world.addRoomSectionCallback(labyrinth, 5, 5,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach unten gehen.", 0, 200);
                }
            }
        });

        world.addRoomSectionCallback(labyrinth, 5, 4,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Rechts von ihnen ist der Eingang zum Garten. Sie können nach oben und nach unten gehen.", 0, 200);
                }
                else if(command == 0 && param == 4)
                {
                    gui.writeln("Gehe in den Garten.", 0, 200);   
                    player.setState(world, 13, 0, 0, 'n');
                }

            }
        });

        world.addRoomSectionCallback(labyrinth, 5, 3,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben, unten und nach links gehen.", 0, 200);
                }

            }
        });
        world.addRoomSectionCallback(labyrinth, 5, 2,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach unten gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 5, 1,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie befinden sich in einer Sackgasse, sie können nach oben gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 4, 3,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 3, 3,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben, unten und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 3, 2,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach links gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 2, 2,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 1, 2,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach unten und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 1, 1,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie befinden sich in einer Sackgasse, sie können nach oben gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 3, 4,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach unten und nach links gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 2, 4,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 1, 4,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 1, 5,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach unten gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 1, 6,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach unten und nach links gehen.", 0, 200);
                }
            }
        });
           world.addRoomSectionCallback(labyrinth, 0, 6,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach rechts gehen.", 0, 200);
                }
            }
        });
              world.addRoomSectionCallback(labyrinth, 0, 7,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach unten gehen.", 0, 200);
                }
            }
        });
                 world.addRoomSectionCallback(labyrinth, 0, 8,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach unten und nach rechts gehen.", 0, 200);
                }
            }
        });
                    world.addRoomSectionCallback(labyrinth, 1, 8,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach links gehen.", 0, 200);
                }
            }
        });
                       world.addRoomSectionCallback(labyrinth, 1, 9,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach unten und nach rechts gehen.", 0, 200);
                }
            }
        });
             world.addRoomSectionCallback(labyrinth, 2, 9,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });   world.addRoomSectionCallback(labyrinth, 3, 9,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });   world.addRoomSectionCallback(labyrinth, 4, 9,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach links und nach rechts gehen.", 0, 200);
                }
            }
        });
        world.addRoomSectionCallback(labyrinth, 5, 9,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben, unten und nach links gehen.", 0, 200);
                }
            }
        });
           world.addRoomSectionCallback(labyrinth, 5, 8,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie befinden sich in einer Sackgasse, sie können nach oben gehen.", 0, 200);
                }
            }
        });
         world.addRoomSectionCallback(labyrinth, 5, 10,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach oben und nach unten gehen.", 0, 200);
                }
            }
        });  world.addRoomSectionCallback(labyrinth, 5, 11,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("Sie stehen im Labyrinth. Sie können nach unten und nach links gehen.", 0, 200);
                }
            }
        });  world.addRoomSectionCallback(labyrinth, 4, 11,
                new Section.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, int command, int param, int self) {
                if (command == 5) {
                    gui.writeln("LEICHE!!!!!!", 0, 200); // passenden text einfügen 
                }
            }
        }); 
           
        return labyrinth;
    }

	@Override
	public void OnUpdate(GUI gui, Event event) {
		// TODO Auto-generated method stub
		
	}

}
