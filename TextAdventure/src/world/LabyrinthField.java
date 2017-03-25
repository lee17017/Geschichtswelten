/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import game.GUI;
import game.GameResources;
import game.Player;
import java.util.HashMap;

/**
 *
 * @author Lee17017
 */
public class LabyrinthField extends Location{
    private HashMap<Integer, Integer> map;	//Move Code 4 bits: l r o u 	
    private int pos=45;//y x 
    public LabyrinthField(String name, int bildID, int soundID, String enterText, String exitText, String observeText){
        super(name, bildID, soundID, enterText, exitText, observeText);
        map = new HashMap<>();
        map.put(45, 7);
        map.put(55, 3);
        map.put(65, 9);
        map.put(64, 12);
        map.put(63, 6);
        map.put(73, 1);
        map.put(35, 13);
        map.put(25, 3);
        map.put(15, 2);
        map.put(34, 12);
        map.put(33, 7);
        map.put(23, 10);
        map.put(22, 12);
        map.put(21, 5);
        map.put(11, 2);
        map.put(43, 9);
        map.put(42, 12);
        map.put(41, 6);
        map.put(51, 3);
        map.put(61, 9);
        map.put(60, 6);
        map.put(70, 3);
        map.put(80, 5);
        map.put(81, 8);
        
        
    }   
    
    public void setPos(int newPos)
    {
        pos = newPos;
    }
    
    public int getPos()
    {
        return pos;
    }
    public int getMoves()
    {
        return map.get(pos);
    }
    
    public void printMoves(GUI gui, World world, Player player)
    {
        if(pos==81)
            return;
      //  String input = "Du stehst im Labyrinth. Du kannst nach ";
        //ArrayList<String> list = new ArrayList<>();
        String input ="";
        if((map.get(pos)&1)!=0)
        {
            input +="unten ";
        }
        if((map.get(pos)&2)!=0)
        {
            input +="oben ";
        }
        if((map.get(pos)&4)!=0)
        {
            input +="rechts ";
        }
        if((map.get(pos)&8)!=0)
        {
            input +="links ";
        }
        
        String[] temp = input.split(" ");
        input = "";
        for(int i=0; i<temp.length-1; i++)
        {
            input += temp[i]+", ";
        }
        if(input.length() != 0)
        {
        input = input.substring(0, input.length()-2);
        input += " oder "+ temp[temp.length-1] + " gehen.";
        input = "Du stehst im Labyrinth. Du kannst nach " + input ;
        
        }
        else
        {
            input = "Sackgasse. Du kannst nach " + temp[0] +" gehen.";
        }
        gui.writeln(input, 0, 200);
    }

    public void onCommand(GUI gui, Player player, World world, int command, int param, int attribute){
        super.onCommand(gui, player, world, command, param, attribute);
        
        if(pos ==81)
        {
            gui.writeln("Du biegst um die Ecke und stehst plötzlich hinter einem großgewachsenen Mann und vor ihm, in einer Ecke des Labyrinths liegt zusammengesunken Edward Graham..", 0, 300);
            
            gui.writeln("Demo Ende.", 0, 1000);
        }
    }
    
    
    
    
}
