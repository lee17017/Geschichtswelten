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

                //---HIER HABE ICH WAS VERÄNDERT
                npc.startDialog(gui, world, player);
                //----
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

        int strasse = Location.createLocation("an der Straße", 0, 0, "Du gehst zurück zum Straßenrand.", "", "Du stehst vor einem hohen, gusseisernen Zaun inmitten dessen sich ein großes, schön verziertes Tor befindet. Gleich hinter dem Zaun versperren ordentlich zurecht gestutzte Hecken die Sicht auf das Grundstück, aber durch das Tor siehst du eine mit Kies bestreute Einfahrt, die bis zur Haustür eines weitläufigen Herrenhauses führt. Das Backsteingebäude hat sicher bereits einige Jahrhunderte gesehen, ist aber trotzdem gut in Stand gehalten. Das muss das Schloss der Grahams sein.");
        int torStrasse = Location.createLocation("Tor bei der Straße", 0, 0, "Du gehst näher an das Tor heran und schaust dich um. Du siehst neben dem Tor am Zaun einen kleinen Kasten hängen.", "", "Du siehst neben dem Tor am Zaun einen kleinen Kasten hängen.");
        int zaun = Location.createLocation("Zaun", 0, 0, "Du gehst zum Zaun. Du siehst einen einen kleinen Kasten am Zaun hängen.", "", "Du stehst beim Zaun. Du siehst einen einen kleinen Kasten am Zaun hängen.");
        int kasten = Location.createLocation("Kasten", 0, 0, "Du gehst zum Kasten.", "", "Der Kasten scheint eine Gegensprechanlage zu sein.");
        int einfahrt = Location.createLocation("Einfahrt", 0, 0, "In der Einfahrt stehend, siehst du vor dir das Anwesen der Grahams", "Du gehst die Einfahrt entlang, steigst eine kleine Treppe hoch.", "Jetzt da du dich hinter dem Zaun und den Hecken befindest, die den Besitz der Grahams umgeben, siehst du, dass das Schloss von einer großen Parkanlage umgeben ist. Links und rechts der Einfahrt führen kleine Wege zwischen farbenprächtigen Blumenbeeten und kleinen Sitzecken vorbei bis hinter das Anwesen.");
        int schlossTor = Location.createLocation("SchlossTor", 0, 0, "Nun stehst du direkt vor der Eingangstür des Schlosses.", "", "Die Haustür ist aus altersdunklem, massivem Holz gefertigt. In der Mitte der Tür hängt ein großer Türklopfer, der einem Löwen nachempfunden ist.");
        int eingangsFlur = Location.createLocation("EingangsFlur", 1, 1, "Langsam gewöhnen sich deine Augen an das Halbdunkel im Inneren des Schlosses. Du befindest dich in einem schmalen, langen Raum, der zu einer weiteren Tür führt.", "", "Du befindest dich in einem schmalen, langen Raum, der zu einer weiteren Tür führt. Der Boden ist mit einem schweren Teppich ausgelegt. An den Wänden hängen etliche Gemälde, deren Motive im Schein der wenigen Leuchter schlecht zu erkennen sind.");
        int tuerFlurEingang = Location.createLocation("Tür Flur Eingangshalle", 1, 1, "Du stehst jetzt direkt vor der Tür, die weiter hinein ins Schloss führt.", "", "Du stehst jetzt direkt vor der Tür, die weiter hinein ins Schloss führt.");
        int eingangsHalle = Location.createLocation("Eingangshalle", 2, 2, "Du betritts die Eingangshalle", "", "Die Eingangshalle ist mit Marmorboden ausgelegt und wird von einem massiven, kristallenen Lüster dominiert, der in der Mitte des zweistöckigen Raumes hängt. Links und rechts führen zwei geschwungene Treppen zu einer Galerie im ersten Stock. Am Fuß jeder Treppe befindet sich jeweils eine Tür, die tiefer ins Erdgeschoss führt. Auf der Seite gegenüber der Tür zum Flur, befindet sich eine zweiflügelige Glastür, die offensichtlich in den Garten führt. ");
        int garten = Location.createLocation("Garten", 0, 0, "Du betrittst den wunderschönen britischen Garten.", "", "Zu deiner rechten Seite siehst du einen Brunnen, zu deiner linken den Eingang zu einem Heckenlabyrinth. Dean, den Gärtner kannst du aber nicht entdecken.");
        int brunnen = Location.createLocation("Brunnen", 0, 0, "Du gehst zum Brunnen.", "", "Der alte Brunnen besitzt eine Kurbel um den Eimer hinab in den Brunnen zu lassen und Wasser hoch zu holen. Der Eimer scheint sich gerade unten im Schacht zu befinden.");
        int labyrinth = Location.createLabyrinth("Labyrinth", 0, 0, "Du gehst in das Heckenlabyrinth. Gerade als du beim Eingang ankommst, hörst du einen Schrei aus dem Labyrinth. Vielleicht solltest du nachschauen, was passiert ist?", "", "Du hast dich verlaufn. RIP");

        //Location.createLocation("", 0, 0, "", "", "");
        //#0 an der Straße
        //game.Sound.playBGM(Location.getLocation(0).getSoundID());
        Location.getLocation(strasse).setAdjacentLocation(torStrasse, new int[]{1}, new int[]{});
        Location.getLocation(strasse).setAdjacentLocation(zaun, new int[]{2}, new int[]{});
        Location.getLocation(strasse).setAdjacentLocation(kasten, new int[]{3}, new int[]{});

        Location.getLocation(strasse).setCallback(0, 5, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, torStrasse);
                gui.writeln("Das Tor ist verschlossen, du kannst nicht zum Schloss gehen.", 0, defaultDelay);
            }
        });
        Location.getLocation(strasse).setCallback(0, 4, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, torStrasse);
                gui.writeln("Das Tor ist verschlossen, du kannst nicht zur Einfahrt gehen.", 0, defaultDelay);
            }
        });

        Location.getLocation(strasse).setCallback(5, 24, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du hörst wie der Taxifahrer sein Auto startet. Als du dich umdrehst, siehst du das Auto inmitten der wunderschönen Landschaft der Highlands in Richtung Edinburgh davonfahren. Sieht so aus als wärst jetzt auf dich allein gestellt.", 0, defaultDelay);
                Location.getLocation(strasse).setCallback(5, 24, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Die Straße ist leer und es gibt auch sonst nichts Auffälliges zu sehen. Rund um das Schloss der Grahams erstreckt sich soweit das Auge reicht nur Weideland, gelegentlich durchbrochen von steilen Klippen oder kleinen Seen. Der Wind trägt die Geräusche einer in der Nähe grasenden Herde Schafe zu dir.", 0, defaultDelay);
                    }
                });
            }
        });
        Location.getLocation(strasse).setExitCallback(new Location.IEnterExitCallback() {
            public void callback(GUI gui, World world, Player player, Location location) {

                Location.getLocation(strasse).setCallback(5, 24, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Die Straße ist leer und es gibt auch sonst nichts Auffälliges zu sehen. Rund um das Schloss der Grahams erstreckt sich soweit das Auge reicht nur Weideland, gelegentlich durchbrochen von steilen Klippen oder kleinen Seen. Der Wind trägt die Geräusche einer in der Nähe grasenden Herde Schafe zu dir.", 0, defaultDelay);
                    }
                });
            }
        });

        //#1 Tor bei der Straße
        Location.getLocation(torStrasse).setAdjacentLocation(strasse, new int[]{0}, new int[]{});
        Location.getLocation(torStrasse).setAdjacentLocation(zaun, new int[]{2}, new int[]{});
        Location.getLocation(torStrasse).setAdjacentLocation(kasten, new int[]{3}, new int[]{});

        Location.getLocation(torStrasse).setCallback(0, 4, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Das Tor ist verschlossen, du kannst nicht zur Einfahrt gehen.", 0, defaultDelay);
            }
        });
        Location.getLocation(torStrasse).setCallback(3, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du kannst den Kasten nicht öffnen.", 0, defaultDelay);
            }
        });

        Location.getLocation(torStrasse).setCallback(3, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du kannst das Tor nicht öffnen.", 0, defaultDelay);
            }
        });

        Location.getLocation(torStrasse).setCallback(5, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, kasten);
                gui.writeln("Der Kasten scheint eine Gegensprechanlage zu sein.", 0, defaultDelay);
            }
        });

        Location.getLocation(torStrasse).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, kasten);
                Location.getLocation(kasten).getCallback(4, 3).callback(gui, world, player, location, param, attribute);
            }
        });

        //#2 Zaun
        Location.getLocation(zaun).setAdjacentLocation(strasse, new int[]{0}, new int[]{});
        Location.getLocation(zaun).setAdjacentLocation(torStrasse, new int[]{1}, new int[]{});
        Location.getLocation(zaun).setAdjacentLocation(kasten, new int[]{3}, new int[]{});

        Location.getLocation(zaun).setCallback(5, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, kasten);
                gui.writeln("Der Kasten scheint eine Gegensprechanlage zu sein.", 0, defaultDelay);
            }
        });

        Location.getLocation(zaun).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, kasten);
                Location.getLocation(kasten).getCallback(4, 3).callback(gui, world, player, location, param, attribute);
            }
        });

        //#3 Kasten
        Location.getLocation(kasten).setAdjacentLocation(strasse, new int[]{0}, new int[]{});
        Location.getLocation(kasten).setAdjacentLocation(torStrasse, new int[]{1}, new int[]{});
        Location.getLocation(kasten).setAdjacentLocation(zaun, new int[]{2}, new int[]{});

        Location.getLocation(kasten).setCallback(3, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du kannst den Kasten nicht öffnen.", 0, defaultDelay);
            }
        });
        Location.getLocation(kasten).setCallback(1, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Keine Antwort... Vielleicht solltest du zuerst klingeln.", 0, defaultDelay);
            }
        });
        Location.getLocation(kasten).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du drückst auf die vermeintliche Klingel.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert. Vielleicht solltest du es noch mal versuchen?", 0, 500);
                Location.getLocation(kasten).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                        gui.writeln("Du drückst noch einmal auf die Klingel.", 0, defaultDelay);
                        gui.writeln(". . .", 500, 100);
                        gui.writeln("Nach einigen Sekunden ertönt tatsächlich eine Stimme aus der Anlage.", 0, 500);
                        Game.Get().getCharacterByPreName("viola").startDialog(gui, world, player);
                        //stuff happening
                        Location.getLocation(torStrasse).setEnterText("");

                        Location.getLocation(strasse).setCallback(0, 4, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                                gui.writeln("Das Tor ist nun offen, du kannst dadurch zur Einfahrt gehen.", 0, defaultDelay);
                            }

                        });
                        for (int i = 0; i < 4; i++) {
                            Location.getLocation(i).setCallback(0, 4, new Location.ICommandCallback() {
                                public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                    player.setLocation(gui, world, torStrasse);
                                }
                            });
                        }

                        Location.getLocation(torStrasse).setEnterCallback(new Location.IEnterExitCallback() {
                            public void callback(GUI gui, World world, Player player, Location location) {
                                gui.writeln("Du durchschreitest eilig das große Tor, das sich auch gleich hinter dir wieder fest verschließt.", 0, defaultDelay);
                                player.setLocation(gui, world, einfahrt);
                            }

                        });
                        Location.getLocation(kasten).setCallback(new int[]{4, 7, 8}, 3, new Location.ICommandCallback() {
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
        Location.getLocation(einfahrt).setAdjacentLocation(schlossTor, new int[]{5}, new int[]{});
        for (int i = 0; i < 4; i++) {
            Location.getLocation(einfahrt).setCallback(0, i, new Location.ICommandCallback() {
                public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                    gui.writeln("Das Tor ist verschlossen.", 0, defaultDelay);
                }
            });
        }
        Location.getLocation(einfahrt).setCallback(new int[]{0, 6}, 25, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Ich sollte zur Eingangshalle gehen.", 0, defaultDelay);
            }
        });
        Location.getLocation(einfahrt).setCallback(5, 5, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Die Backsteinfassade des alten Schlosses ist zu großen Teilen mit Efeu bedeckt und kleine Türme zieren das Dach des weit verzweigten Gebäudes. Die Eingangstür ist über eine Treppe zu erreichen, die gleich ans Ende der Einfahrt grenzt.", 0, defaultDelay);
            }
        });
        Location.getLocation(einfahrt).setCallback(new int[]{0, 5}, 6, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Die Blumenbeete umfassen eine Vielzahl verschiedener Blumen in allen Farben, deren lieblicher Duft einige Schmetterlinge angelockt hat. Wer auch immer sich um diese Beete kümmert, muss ein begnadeter Gärtner sein", 0, defaultDelay);
            }
        });

        //#5 Schloss
        Location.getLocation(schlossTor).setAdjacentLocation(einfahrt, new int[]{4}, new int[]{});

        Location.getLocation(schlossTor).setCallback(3, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du drückst die Klinke der Haustür herunter und öffnest die schwere Tür. Noch an die Helligkeit der in Sonnenlicht gefluteten Einfahrt gewohnt, kannst du den Raum hinter der Tür nur schemenhaft erkennen. Vorsichtig trittst du ein und schließt die Tür hinter dir.", 0, defaultDelay);
                player.setLocation(gui, world, eingangsFlur);
            }
        });
        Location.getLocation(schlossTor).setCallback(9, -1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Der Türklopfer schlägt mit einem lauten Knall, der in inneren des Schlosses tausendfach nachhallt, gegen die Haustür.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert.", 0, 500);
            }
        });
        Location.getLocation(schlossTor).setCallback(9, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Der Türklopfer schlägt mit einem lauten Knall, der in inneren des Schlosses tausendfach nachhallt, gegen die Haustür.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert.", 0, 500);
            }
        });

        Location.getLocation(schlossTor).setCallback(new int[]{4, 9}, 7, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Der Türklopfer schlägt mit einem lauten Knall, der in inneren des Schlosses tausendfach nachhallt, gegen die Haustür.", 0, defaultDelay);
                gui.writeln(". . .", 500, 100);
                gui.writeln("Nichts passiert.", 0, 500);
            }
        });

        Location.getLocation(schlossTor).setCallback(6, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Was hast du mit der Tür vor? Wie und wohin willst du die denn tragen?", 0, defaultDelay);
            }
        });

        //#6 Eingangsflur
        Location.getLocation(eingangsFlur).setAdjacentLocation(tuerFlurEingang, new int[]{1}, new int[]{});
        Location.getLocation(eingangsFlur).setCallback(5, 8, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du gehst näher heran und schaust dir die Gemälde genauer an. Bei den meisten scheint es sich um Familienfotos zu handeln. Ein Hochzeitfoto sticht dir ins Auge und du glaubst zu erkennen, dass es sich bei dem Mann um den Hausherrn Edward Graham handelt. Die rothaarige Frau an seiner Seite erkennst du hingegen nicht.", 0, defaultDelay);
            }
        });

        //#7 Tür Flur Eingangshalle
        Location.getLocation(tuerFlurEingang).setAdjacentLocation(6, new int[]{9}, new int[]{});

        Location.getLocation(tuerFlurEingang).setCallback(new int[]{3, 4}, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du öffnest die Tür und trittst hindurch. Du findest dich in einer großen Halle wieder, von der mehrere Türen und Treppen abgehen. In der Mitte der Halle befinden sich drei Personen, ein Mann und zwei Frauen.", 0, defaultDelay);
                player.setLocation(gui, world, eingangsHalle);
            }
        });

        //#8 Eingangshalle  
        Location.getLocation(eingangsHalle).setCallback(0, 9, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du bist doch gerade erst angekommen, wieso willst du schon gehen?", 0, defaultDelay);
            }
        });

        Location.getLocation(eingangsHalle).setEnterCallback(new Location.IEnterExitCallback() {
            public void callback(GUI gui, World world, Player player, Location location) {

             //   Game.Get().getCharacterByPreName("viola").startDialog(gui, world, player);
                Location.getLocation(eingangsHalle).setEnterCallback(new Location.IEnterExitCallback() {
                    public void callback(GUI gui, World world, Player player, Location location) {

                    }

                });
            }

        });
        /*
        
        Location.getLocation(eingangsHalle).setCallback(1, 10, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Ellito gespräch", 0, defaultDelay);
            }
        });

        Location.getLocation(eingangsHalle).setCallback(1, 11, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("viola dialog", 0, defaultDelay);
            }
        });

        Location.getLocation(eingangsHalle).setCallback(1, 12, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("scarllet dialog", 0, defaultDelay);
            }
        });*/

        Location.getLocation(eingangsHalle).setCallback(0, 26, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du solltest wahrscheinlich nicht gleich am Anfang unaufgefordert im Schloss rumstöbern. Wenn du zu früh auffliegst, kannst du deinen Auftrag nicht abschließen.", 0, defaultDelay);
            }
        });
        Location.getLocation(eingangsHalle).setCallback(0, 1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du gehst durch die Gartentür.", 0, defaultDelay);
                player.setLocation(gui, world, garten);
            }
        });
        Location.getLocation(eingangsHalle).setAdjacentLocation(garten, new int[]{13}, new int[]{});
        //#9 Garten

        Location.getLocation(garten).setAdjacentLocation(eingangsHalle, new int[]{14}, new int[]{});
        Location.getLocation(garten).setAdjacentLocation(brunnen, new int[]{15}, new int[]{});
        Location.getLocation(garten).setAdjacentLocation(labyrinth, new int[]{16}, new int[]{});

        Location.getLocation(garten).setCallback(0, 14, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du solltest zuerst mit Dean sprechen.", 0, defaultDelay);
            }
        });

        Location.getLocation(garten).setCallback(0, 5, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Du solltest zuerst mit Dean sprechen.", 0, defaultDelay);
            }
        });

        Location.getLocation(garten).setCallback(0, 18, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, brunnen);
            }
        });
        Location.getLocation(garten).setCallback(0, 17, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                player.setLocation(gui, world, labyrinth);
            }
        });

        //#10 Brunen
        Location.getLocation(brunnen).setAdjacentLocation(garten, new int[]{13}, new int[]{});
        Location.getLocation(brunnen).setAdjacentLocation(labyrinth, new int[]{16}, new int[]{});

        Location.getLocation(brunnen).setCallback(13, 23, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                Location.getLocation(brunnen).getCallback(12, 22).callback(gui, world, player, location, param, attribute);
            }
        });

        Location.getLocation(brunnen).setCallback(new int[]{11, 4}, 21, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                gui.writeln("Du drehst an der Kurbel, das Seil wickelt sich auf und schließlich kommt tatsächlich der Eimer zum Vorschein. Du stellst ihn am Brunnenrand ab.", 0, defaultDelay);

                Location.getLocation(brunnen).setObserveText("Der alte Brunnen besitzt eine Kurbel um den Eimer hinab in den Brunnen zu lassen und Wasser hoch zu holen. Der Eimer steht am Brunnenrand.");

                Location.getLocation(brunnen).setCallback(new int[]{11, 4}, 21, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Du hast den Eimer bereits hochgezogen.", 0, defaultDelay);
                    }
                });

                Location.getLocation(brunnen).setCallback(5, 22, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Wie zu erwarten ist der Eimer mit Wasser gefüllt. Allerdings siehst du auch etwas Metallenes am Grund schimmern.", 0, defaultDelay);
                    }
                });

                Location.getLocation(brunnen).setCallback(12, 22, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Du leerst den Eimer und es stellt sich heraus, dass das glitzernde Objekt ein Amulett ist.", 0, defaultDelay);
                        gui.writeln("Du nimmst das Amulett und öffnest es. Im Amulett findest ein Foto von einer rothaarigen Frau, die verblüffende Ähnlichkeit mit Scarlett hat. Da das Foto so gut erhalten ist, kann das Amulett noch nicht lange im Brunnen gelegen haben.", 0, defaultDelay);

                        Location.getLocation(brunnen).setCallback(10, 22, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Das Amulett ist nicht mehr im Eimer.", 0, defaultDelay);
                            }
                        });

                        Location.getLocation(brunnen).setCallback(12, 22, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Der Eimer ist bereits leer.", 0, defaultDelay);
                            }
                        });

                        Location.getLocation(brunnen).setCallback(5, 22, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Ein leerer Eimer.", 0, defaultDelay);
                            }
                        });
                        Location.getLocation(brunnen).setCallback(new int[]{2, 3, 4, 5, 11}, 23, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Du nimmst das Amulett und öffnest es. Im Amulett findest ein Foto von einer rothaarigen Frau, die verblüffende Ähnlichkeit mit Scarlett hat. Da das Foto so gut erhalten ist, kann das Amulett noch nicht lange im Brunnen gelegen haben.", 0, defaultDelay);
                            }
                        });

                    }
                });
                Location.getLocation(brunnen).setCallback(13, 23, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        Location.getLocation(brunnen).getCallback(12, 22).callback(gui, world, player, location, param, attribute);
                    }
                });
                Location.getLocation(brunnen).setCallback(10, 22, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Du greifst in den Eimer rein und greifst nach dem Gegenstand aus Metall. Nach dem du es aus dem Wasser geholt hast, siehst du, dass es sich um ein Amulett handelt.", 0, defaultDelay);
                        gui.writeln("Du nimmst das Amulett und öffnest es. Im Amulett findest ein Foto von einer rothaarigen Frau, die verblüffende Ähnlichkeit mit Scarlett hat. Da das Foto so gut erhalten ist, kann das Amulett noch nicht lange im Brunnen gelegen haben.", 0, defaultDelay);

                        Location.getLocation(brunnen).setCallback(10, 22, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Das Amulett ist nicht mehr im Eimer.", 0, defaultDelay);
                            }
                        });

                        Location.getLocation(brunnen).setCallback(12, 22, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Du hast keinen Grund den Eimer zu leeren.", 0, defaultDelay);
                            }
                        });

                        Location.getLocation(brunnen).setCallback(5, 22, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Ein mit Wasser gefüllter Eimer.", 0, defaultDelay);
                            }
                        });
                        Location.getLocation(brunnen).setCallback(new int[]{2, 3, 4, 5, 11}, 23, new Location.ICommandCallback() {
                            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                                gui.writeln("Du nimmst das Amulett und öffnest es. Im Amulett findest ein Foto von einer rothaarigen Frau, die verblüffende Ähnlichkeit mit Scarlett hat. Da das Foto so gut erhalten ist, kann das Amulett noch nicht lange im Brunnen gelegen haben.", 0, defaultDelay);
                            }
                        });
                    }
                });
                Location.getLocation(brunnen).setCallback(new int[]{2, 10}, 23, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        Location.getLocation(brunnen).getCallback(10, 22).callback(gui, world, player, location, param, attribute);
                    }
                });
                Location.getLocation(brunnen).setCallback(2, 22, new Location.ICommandCallback() {
                    public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                        gui.writeln("Du hast keinen Grund einen Eimer mit dir zuführen .", 0, defaultDelay);
                    }
                });
            }
        });

        Location.getLocation(brunnen).setCallback(2, 22, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                Location.getLocation(brunnen).getCallback(4, 21).callback(gui, world, player, location, param, attribute);
            }
        });

        //#11 Labyrinth
        LabyrinthField labyrinthL = (LabyrinthField) Location.getLocation(labyrinth);

        labyrinthL.setCallback(0, 16, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                labyrinthL.printMoves(gui, world, player);
            }
        });

        labyrinthL.setCallback(0, 13, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Vielleicht solltest du nachschauen, was passiert ist?", 0, defaultDelay);
            }
        });

        labyrinthL.setCallback(0, 15, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                gui.writeln("Vielleicht solltest du nachschauen, was passiert ist?", 0, defaultDelay);
            }
        });

        labyrinthL.setCallback(5, -1, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                labyrinthL.printMoves(gui, world, player);
            }
        });

        labyrinthL.setCallback(0, 17, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                if ((labyrinthL.getMoves() & 8) != 0) {
                    labyrinthL.setPos(labyrinthL.getPos() - 1);
                    labyrinthL.printMoves(gui, world, player);
                } else {
                    gui.writeln("Du kannst in diese Richtung nicht gehen", 0, defaultDelay);
                }

            }
        });

        labyrinthL.setCallback(0, 18, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {
                if (labyrinthL.getPos() == 45) {
                    gui.writeln("Dies ist der Weg zum Garten. Vielleicht solltest du nachschauen, was passiert ist?", 0, defaultDelay);
                    return;
                }

                if ((labyrinthL.getMoves() & 4) != 0) {
                    labyrinthL.setPos(labyrinthL.getPos() + 1);
                    labyrinthL.printMoves(gui, world, player);
                } else {
                    gui.writeln("Du kannst in diese Richtung nicht gehen", 0, defaultDelay);
                }

            }
        });

        labyrinthL.setCallback(0, 19, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                if ((labyrinthL.getMoves() & 2) != 0) {
                    labyrinthL.setPos(labyrinthL.getPos() + 10);
                    labyrinthL.printMoves(gui, world, player);
                } else {
                    gui.writeln("Du kannst in diese Richtung nicht gehen", 0, defaultDelay);
                }

            }
        });

        labyrinthL.setCallback(0, 20, new Location.ICommandCallback() {
            public void callback(GUI gui, World world, Player player, Location location, int param, int attribute) {

                if ((labyrinthL.getMoves() & 1) != 0) {
                    labyrinthL.setPos(labyrinthL.getPos() - 10);
                    labyrinthL.printMoves(gui, world, player);
                } else {
                    gui.writeln("Du kannst in diese Richtung nicht gehen", 0, defaultDelay);
                }

            }
        });

        return true;
    }
}
