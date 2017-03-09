package game;

import java.io.*;
import java.util.Scanner;

public class Parser {

    static Scanner sc = new Scanner(System.in);
    static String[][] command;
    static String[][] params;

    private static String[][] readFile(String filename)
    {
        String[][] temp = null;
        int i = 0;
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            String line;
            while ((line = file.readLine()) != null) {
                if (!line.equals("")) {
                    i++;
                }
            }
            temp = new String[i][];
            file.seek(0);
            i = 0;
            while ((line = file.readLine()) != null) {
                if (!line.equals("")) {
                    temp[i] = line.split(" ");
                    for(int j=0; j<temp[i].length; j++)
                        temp[i][j] = "(.*)" + temp[i][j] + "(.*)";
                    
                    i++;
                }
            }
            file.close();
          
        } catch (IOException ex) {
            System.out.println("IO Exception; " + filename);
        }
        return temp;
    }
    
    public static void init()
    {
        command = readFile("command.txt");
        params = readFile("param.txt");
    }
    // returns 4-digit number code (first 2 digits=command, last 2 digits=paramter)
    public static int command(String input) {
        if("".equals(input))
            return -1;
        input = input.toLowerCase();
        
        //search for command
        for (int i = 0; i < command.length; i++) {
            for (int j = 0; j < command[i].length; j++) {
                if (input.matches(command[i][j])) 
                    return i;
                
            }
        }

        return -1;
    }

    public static int param(String input) {
        input = input.toLowerCase();
        
        //search for parameter of command
        for (int j = 0; j < params.length; j++) {
            for(int h = 0; h < params[j].length; h++)
            {
                if (input.matches(params[j][h])) {
                    return j;
                }
            }
        }
        
        return -1;
    }
    
    public static void main(String[] args)
    {
        init();
       
        System.out.println(command[4][1]);
    }

    //Muss alles in Handler
    public static void handle(String input, GUI a)
    {
        if("".equals(input))
        {
            //a.write("", 0, 0);
            return;
        }
        
        int command = command(input);
        int param = param(input);
        
        if(command == -1){
        	return;
            //System.out.println("ned oder");
        }
        //else if(inp < 100){
        	//System.out.println("hier");
        	//Game.Get().OnCommand(command, param);
        //}
        //else{System.out.println("hier2");
        //	Game.Get().OnCommand(inp/100, inp%100);
        //}

        //a.repaint(); // wenn bg sich ändert
    }
}