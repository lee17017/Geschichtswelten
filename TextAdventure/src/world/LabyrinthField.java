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
    private HashMap<Integer, Integer> map;		
    private int pos=11;
    public LabyrinthField(String name, int bildID, int soundID, String enterText, String exitText, String observeText){
        super(name, bildID, soundID, enterText, exitText, observeText);
        map = new HashMap<>();
        map.put(11, 15);
        map.put(12, 8);
        map.put(10, 4);
        map.put(1, 2);
        map.put(21, 3);
        map.put(31, 1);
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
        input = input.substring(0, input.length()-2);
        input += " oder "+ temp[temp.length-1] + " gehen.";
        input = "Du stehst im Labyrinth. Du kannst nach " + input ;
        gui.writeln(input, 0, 200);
    }

    public void onCommand(GUI gui, Player player, World world, int command, int param, int attribute){
        super.onCommand(gui, player, world, command, param, attribute);
        if(pos ==31)
        {
            gui.writeln("LEICHE!!", 0, 1000);
        }
    }
    
    
    
    
}
