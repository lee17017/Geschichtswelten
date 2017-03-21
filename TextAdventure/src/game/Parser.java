package game;

import java.io.*;
import java.util.Scanner;

public class Parser {

    static Scanner sc = new Scanner(System.in);
    static String[][] command;
    static String[][] params;
    static String[][] attribute;
    private static String[][] readFile(String filename)
    {
        String[][] temp = null;
        int i = 0;
        try {
            RandomAccessFile file = new RandomAccessFile("src/ressources/txt/"+filename, "r");
            String line;
            // kann man auch entfernen wenn man fixe ZeilenAnzahl hat
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
                        temp[i][j] = "(.*)" + temp[i][j].replace('_', ' ') + "(.*)";
                    
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
        attribute = readFile("attribute.txt");
    }
    
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
          if("".equals(input))
            return -1;
        input = input.toLowerCase();
        
        //search for parameter of command
        for (int i = 0; i < params.length; i++) {
            for(int j = 0; j < params[i].length; j++)
            {
                if (input.matches(params[i][j])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static int attribute(String input) {
          if("".equals(input))
            return -1;
        input = input.toLowerCase();
        
        //search for parameter of command
        for (int i = 0; i < attribute.length; i++) {
            for(int j = 0; j < attribute[i].length; j++)
            {
                if (input.matches(attribute[i][j])) {
                    return i;
                }
            }
        }
        return -1;
    }



}