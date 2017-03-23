package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import game.GUI;
import game.Game;
import game.GameResources;
import game.NPC;
import game.Player;

public class World {

    public boolean Initialize() {
        int defaultDelay = 200;

        ///--- Set default callbacks
        /// gehen
        Location.setDefaultCallback(0, new Location.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                if (param == -1) {
                    gui.writeln(GameResources.defaultNoLocationText, 0, defaultDelay);
                    return;
                }

                if (location.getAdjacentLocations(param).size() == 0) {
                    gui.writeln(GameResources.defaultInvalidLocationText, 0, defaultDelay);
                    return;
                }

                // get all the locations corresponding to this parameter
                ArrayList<Integer> locs = location.getAdjacentLocations(param);

                if (locs.size() == 0) {
                    gui.writeln(GameResources.defaultInvalidLocationText, 0, defaultDelay);
                    return;
                }

                // sort out locations without the specific attribute
                if (attribute != -1) {
                    for (int i = locs.size() - 1; i >= 0; --i) {
                        if (!location.getAdjacentLocationAttributes(locs.get(i)).contains(attribute)) {
                            locs.remove(i);
                        }
                    }
                }

                // return if the attribute doesnt correspond to the location(s)
                if (locs.size() == 0) {
                    gui.writeln(GameResources.defaultInvalidAttributeText, 0, defaultDelay);
                    return;
                } // return if there are multiple solutions
                else if (locs.size() > 1) {
                    gui.writeln(GameResources.defaultMultiplePossibilitiesText, 0, defaultDelay);
                    return;
                }

                // go to that location
                int loc_id = locs.get(0);
                player.setLocation(gui, world, loc_id);
            }
        });

        /// reden
        Location.setDefaultCallback(1, new Location.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                if (param == -1) {
                    gui.writeln(GameResources.defaultInvalidDiscussionPartnerText, 0, defaultDelay);
                    return;
                }

                ArrayList<NPC> npcArr = Game.Get().getCharacters();
                for (int i = npcArr.size() - 1; i >= 0; --i) {
                    if (npcArr.get(i).param != param) {
                        npcArr.remove(i);
                    }
                }

                if (npcArr.size() == 0) {
                    gui.writeln(GameResources.defaultInvalidDiscussionPartnerText, 0, defaultDelay);
                    return;
                }

                if (npcArr.size() > 1) {
                    gui.writeln("Callback Error: multiple possibilities", 0, 0);
                    return;
                }

                NPC npc = npcArr.get(0);
                if (npc.location != player.getLocation()) {
                    gui.writeln(String.format(GameResources.defaultDiscussionPartnerNotHereText, npc.preName), 0, defaultDelay);
                    return;
                }

                npc.startDialog(gui, player);
            }
        });

        /// nehmen, öffnen, benutzen, bewegen
        Location.setDefaultCallback(new int[]{2, 3, 4, 6}, new Location.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                if (param == -1) {
                    gui.writeln(GameResources.defaultNoObjectText, 0, defaultDelay);
                    return;
                }
                gui.writeln(GameResources.defaultNotAllowedText, 0, defaultDelay);
            }
        });

        /// umschauen
        Location.setDefaultCallback(5, new Location.ICommandCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                if (location.getAdjacentLocations(param).size() > 0) {
                    gui.writeln(GameResources.defaultTooFarAwayText, 0, defaultDelay);
                    return;
                }

                if (param != -1) {
                    gui.writeln(GameResources.defaultHereNotAllowedText, 0, defaultDelay);
                    return;
                }

                location.observe(gui);
            }
        });

        /// onEnter
        Location.setDefaultEnterCallback(new Location.IEnterExitCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, Location location) {
                gui.writeln(location.getEnterText(), 0, defaultDelay);
            }
        });

        /// onExit
        Location.setDefaultExitCallback(new Location.IEnterExitCallback() {
            @Override
            public void callback(GUI gui, World world, Player player, Location location) {
                gui.writeln(location.getExitText(), 0, defaultDelay);
            }
        });

        Location.createLocation("an der Straße", 0, 0, "Du gehst zurück zum Straßenrand.", "", "Du stehst vor einem hohen, gusseisernen Zaun inmitten dessen sich ein großes, schön verziertes Tor befindet. Gleich hinter dem Zaun versperren ordentlich zurecht gestutzte Hecken die Sicht auf das Grundstück, aber durch das Tor siehst du eine mit Kies bestreute Einfahrt die bis zur Haustür eines weitläufigen Herrenhauses führt. Das Backsteingebäude hat sicher bereits einige Jahrhunderte gesehen, ist aber trotzdem gut in Stand gehalten. Das muss das Schloss der Grahams sein.");
        Location.createLocation("Tor bei der Straße", 0, 0, "Du gehst näher an das Tor heran und schaust dich um. Du siehst neben dem Tor am Zaun einen kleinen Kasten hängen.", "", "Du siehst neben dem Tor am Zaun einen kleinen Kasten hängen.");
        Location.createLocation("Zaun", 0, 0, "Du gehst zum Zaun. Du siehst einen einen kleinen Kasten am Zaun hängen.", "", "Du stehst beim Zaun. Du siehst einen einen kleinen Kasten am Zaun hängen.");
        Location.createLocation("Kasten", 0, 0, "Du gehst zum Kasten.", "", "Der Kasten scheint eine Gegensprechanlage zu sein.");
        Location.createLocation("Einfahrt", 0, 0, "In der Einfahrt stehend, siehst du vor dir das Schloss.//ändern", "Du gehst die Einfahrt entlang, steigst eine kleine Treppe hoch.", "Jetzt da du dich hinter dem Zaun und den Hecken befindest, die den Besitz der Grahams umgeben, siehst du, dass das Schloss von einer großen Parkanlage umgeben ist. Links und rechts der Einfahrt führen kleine Wege zwischen farbenprächtigen Blumenbeeten und kleinen Sitzecken vorbei bis hinter das Anwesen.");
        Location.createLocation("SchlossTor", 0, 0, "Nun stehst du direkt vor der Eingangstür des Schlosses.", "", "Die Haustür ist aus altersdunklem, massivem Holz gefertigt. In der Mitte der Tür hängt ein großer Türklopfer, der einem Löwen nachempfunden ist.");
        Location.createLocation("EingangsFlur", 0, 0, "Langsam gewöhnen sich deine Augen an das Halbdunkel im Inneren des Schlosses. Du befindest dich in einem schmalen, langen Raum, der zu einer weiteren Tür führt.", "", "Du befindest dich in einem schmalen, langen Raum, der zu einer weiteren Tür führt. Der Boden ist mit einem schweren Teppich ausgelegt. An den Wänden hängen etliche Gemälde, deren Motive im Schein der wenigen Leuchter schlecht zu erkennen sind.");
        Location.createLocation("Tür Flur Eingangshalle", 0, 0, "Du stehst jetzt direkt vor der Tür, die weiter hinein ins Schloss führt.", "", "Du stehst jetzt direkt vor der Tür, die weiter hinein ins Schloss führt.");
        Location.createLocation("Eingangshalle", 0, 0, "Eingangshalle", "", "");

        //Location.createLocation("", 0, 0, "", "", "");
        //#0 an der Straße
        //game.Sound.playBGM(Location.getLocation(0).getSoundID());
        Location.getLocation(0).setAdjacentLocation(1, new int[]{1}, new int[]{});
        Location.getLocation(0).setAdjacentLocation(2, new int[]{2}, new int[]{});
        Location.getLocation(0).setAdjacentLocation(3, new int[]{3}, new int[]{});

        Location.getLocation(0).setCallback(0, 4, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Das Tor ist verschlossen, du kannst nicht zur Einfahrt gehen.", 0, defaultDelay);
            }
        });

        //#1 Tor bei der Straße
        Location.getLocation(1).setAdjacentLocation(0, new int[]{0}, new int[]{});
        Location.getLocation(1).setAdjacentLocation(2, new int[]{2}, new int[]{});
        Location.getLocation(1).setAdjacentLocation(3, new int[]{3}, new int[]{});

        Location.getLocation(1).setCallback(0, 4, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Das Tor ist verschlossen, du kannst nicht zur Einfahrt gehen.", 0, defaultDelay);
            }
        });

        Location.getLocation(1).setCallback(5, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, 3);
                gui.writeln("Der Kasten scheint eine Gegensprechanlage zu sein.", 0, defaultDelay);
            }
        });

        //#2 Zaun
        Location.getLocation(2).setAdjacentLocation(0, new int[]{0}, new int[]{});
        Location.getLocation(2).setAdjacentLocation(1, new int[]{1}, new int[]{});
        Location.getLocation(2).setAdjacentLocation(3, new int[]{3}, new int[]{});

        //#3 Kasten
        Location.getLocation(3).setAdjacentLocation(0, new int[]{0}, new int[]{});
        Location.getLocation(3).setAdjacentLocation(1, new int[]{1}, new int[]{});
        Location.getLocation(3).setAdjacentLocation(2, new int[]{2}, new int[]{});

        Location.getLocation(3).setCallback(7, -1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du drückst auf die vermeintliche Klingel.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert. Vielleicht solltest du es noch mal versuchen?", 0, 500);
                Location.getLocation(3).setCallback(7, -1, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                        gui.writeln("Du drückst noch einmal auf die Klingel.", 0, defaultDelay);
                        gui.writeln(". . .", 500, 100);
                        gui.writeln("Nach einigen Sekunden ertönt tatsächlich eine Stimme aus der Anlage.", 0, 500);

                        gui.writeln("DialogStuff - Kaum ist die Stimme aus der Gegensprechanlage verstummt, öffnet sich auch schon das Tor vor dir.", 0, defaultDelay);
                        //stuff happening
                        Location.getLocation(1).setEnterText("Das Tor ist nun offen, du kannst zur Einfahrt gehen.");

                        Location.getLocation(0).setCallback(0, 4, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                                gui.writeln("Das Tor ist nun offen, du kannst dadurch zur Einfahrt gehen.", 0, defaultDelay);
                            }

                        });
                        Location.getLocation(1).setCallback(0, 4, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Du durchschreitest eilig das große Tor, das sich auch gleich hinter dir wieder fest verschließt.", 0, defaultDelay);
                                player.setLocation(gui, world, 4);
                            }

                        });
                        Location.getLocation(3).setCallback(7, -1, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                                gui.writeln("Du hast schon oft genug geklingelt.", 0, defaultDelay);
                                //stuff happening
                            }
                        });
                    }

                });

            }
        });
        Location.getLocation(3).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du drückst auf die vermeintliche Klingel.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert. Vielleicht solltest du es noch mal versuchen?", 0, 500);
                Location.getLocation(3).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                        gui.writeln("Du drückst noch einmal auf die Klingel.", 0, defaultDelay);
                        gui.writeln(". . .", 500, 100);
                        gui.writeln("Nach einigen Sekunden ertönt tatsächlich eine Stimme aus der Anlage.", 0, 500);

                        gui.writeln("DialogStuff - Kaum ist die Stimme aus der Gegensprechanlage verstummt, öffnet sich auch schon das Tor vor dir.", 0, defaultDelay);
                        //stuff happening
                        Location.getLocation(1).setEnterText("Das Tor ist nun offen, du kannst zur Einfahrt gehen.");

                        Location.getLocation(0).setCallback(0, 4, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                                gui.writeln("Das Tor ist nun offen, du kannst dadurch zur Einfahrt gehen.", 0, defaultDelay);
                            }

                        });
                        Location.getLocation(1).setCallback(0, 4, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Du durchschreitest eilig das große Tor, das sich auch gleich hinter dir wieder fest verschließt.", 0, defaultDelay);
                                player.setLocation(gui, world, 4);
                            }

                        });
                        Location.getLocation(3).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                                gui.writeln("Du hast schon oft genug geklingelt.", 0, defaultDelay);
                                //stuff happening
                            }
                        });
                    }

                });

            }
        });

        //#4 Einfahrt
        Location.getLocation(4).setAdjacentLocation(5, new int[]{5}, new int[]{});

        Location.getLocation(4).setCallback(0, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Das Tor ist verschlossen.", 0, defaultDelay);
            }
        });
        Location.getLocation(4).setCallback(5, 5, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Die Backsteinfassade des alten Schlosses ist zu großen Teilen mit Efeu bedeckt und kleine Türme zieren das Dach des weit verzweigten Gebäudes. Die Eingangstür ist über eine Treppe zu erreichen, die gleich ans Ende der Einfahrt grenzt.", 0, defaultDelay);
            }
        });
        Location.getLocation(4).setCallback(new int[]{0, 5}, 6, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Wunderschöne Blumen", 0, defaultDelay);
            }
        });

        //#5 Schloss
        Location.getLocation(5).setAdjacentLocation(4, new int[]{4}, new int[]{});

        Location.getLocation(5).setCallback(3, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du drückst die Klinke der Haustür herunter und öffnest die schwere Tür. Noch an die Helligkeit der in Sonnenlicht gefluteten Einfahrt gewohnt, kannst du den Raum hinter der Tür nur schemenhaft erkennen. Vorsichtig trittst du ein und schließt die Tür hinter dir.", 0, defaultDelay);
                player.setLocation(gui, world, 6);
            }
        });
        Location.getLocation(5).setCallback(9, -1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Der Türklopfer schlägt mit einem lauten Knall, der in inneren des Schlosses tausendfach nachhallt, gegen die Haustür.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert.", 0, 500);
            }
        });
        Location.getLocation(5).setCallback(9, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Der Türklopfer schlägt mit einem lauten Knall, der in inneren des Schlosses tausendfach nachhallt, gegen die Haustür.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert.", 0, 500);
            }
        });

        Location.getLocation(5).setCallback(new int[]{4, 9}, 7, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Der Türklopfer schlägt mit einem lauten Knall, der in inneren des Schlosses tausendfach nachhallt, gegen die Haustür.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert.", 0, 500);
            }
        });

        Location.getLocation(5).setCallback(6, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Was hast du mit der Tür vor? Wie und wohin willst du die denn tragen?", 0, defaultDelay);
            }
        });

        //#6 Eingangsflur
        Location.getLocation(6).setAdjacentLocation(7, new int[]{1}, new int[]{});
        Location.getLocation(6).setCallback(5, 8, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du erkennst auf den Gemälden den Hausherren Edward Graham und seine Tochter Scarlett Graham.", 0, defaultDelay);
            }
        });

        //#7 Tür Flur Eingangshalle
        Location.getLocation(7).setAdjacentLocation(6, new int[]{9}, new int[]{});

        Location.getLocation(7).setCallback(new int[]{3,4}, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du öffnest die Tür und trittst hindurch. Du findest dich in einer großen Halle wieder, von der mehrere Türen und Treppen abgehen. In der Mitte der Halle befinden sich drei Personen, ein Mann und zwei Frauen.", 0, defaultDelay);
                player.setLocation(gui, world, 8);
            }
        });
        
        //#8 Eingangshalle
        

        return true;
    }
}
