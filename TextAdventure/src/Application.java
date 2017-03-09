import game.GUI;
import game.Game;
import game.Parser;
public class Application {
    static GUI gui;
    
    public static void main(String[] args) {
        gui = new GUI();
        Parser.init();
        Game.Get().setGUI(gui);
        Game.Get().Start();
    }
}
