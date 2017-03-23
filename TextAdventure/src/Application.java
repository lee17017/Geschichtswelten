import game.GUI;
import game.Game;
import game.Parser;
import game.Sound;

public class Application {
    static GUI gui;
    
    public static void main(String[] args) {
        gui = new GUI();
        Parser.init();
        Sound.init();
        Game.Get().setGUI(gui);
        Game.Get().Start();
    }
}
