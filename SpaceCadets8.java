/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacecadets8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.script.ScriptEngine;

/**
 *
 * @author alexa
 */
public class SpaceCadets8 {
    private static int numberOfConnections = 0;
    private static int list[] = new int[1000];
    private static int distance[] = new int[1000];
    private static boolean inqueue[] = new boolean [1000];
    private static int peak[] = new int[200000];
    private static int goldByConnection[] = new int[200000];
    private static int next[] = new int[200000];
    private static int queue[] = new int[200000];
    private static int counter[] = new int[1000];
    private static int steps[] = new int[1000];
    private static int numberOfRooms, numberOfCoonections, startRoom, connections = 0;
    private static boolean negativ = false;
    public static void connect(int roomToConnect, int roomToConnectTo, int gold){
        connections++;
        peak[connections] = roomToConnectTo;
        goldByConnection[connections] = gold;
        next[connections] = list[roomToConnect];
        list[roomToConnect] = connections;
        
    }
    
    public static void createLabirinth(String fileName){
        File inputFile = new File(fileName);
         BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(inputFile));
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        if(reader!=null){
            try{
            String line = reader.readLine();
            numberOfRooms = Integer.parseInt(line.substring(0, line.indexOf(' ')));
            numberOfConnections = Integer.parseInt(line.substring( line.indexOf(' ')+1,
                    line.lastIndexOf(' ')));
            startRoom = Integer.parseInt(line.substring(line.lastIndexOf(' ')+1));
            for(int i = 0; i < numberOfConnections; i++){
                String param = reader.readLine();
                
                int firstRoom = Integer.parseInt(param.substring(0, param.indexOf(' ')));
                int lastRoom = Integer.parseInt(param.substring(
                        param.indexOf(' ')+1, param.lastIndexOf(' ')));
                int gold = Integer.parseInt(param.substring(param.lastIndexOf(' ')+1));
                connect(firstRoom, lastRoom, gold);
            }
            }catch(IOException e){
                System.out.println("Problem in reading from file");
            }
        } 
    }
    
    public static void init(){
        for(int i = 0; i<numberOfRooms; i++){
            distance[i] = 999999999;
            steps[i] = 999999999;
        }
    }
    
    public static void bellmanFord(){
    int first=1,last=0,position,y,x, ca;
    x= startRoom;
    distance[x]=0;
    inqueue[x]=true;
    counter[x]+=1;
    queue[++last] = x;
    while (first<=last)
    {
        x=queue[first++];
        inqueue[x]=false;
        position=list[x];
        while (position!=0)
        {
            y=peak[position];
            ca=goldByConnection[position];
            if ((distance[x]+ca)<distance[y])
            {
                distance[y]=distance[x]+ca;
                steps[y] = steps[x] + 1;
                if (!inqueue[y])
                {
                    queue[++last]=y;
                    inqueue[y]=true;
                    counter[y]++;
                    if (counter[y]==numberOfRooms)
                    {
                        negativ=true;
                        return;
                    }
                }
            }
            position=next[position];
        }
    }
    }
    
    public static void playGame(){
        int currentRoom = startRoom;
        int currentGold = 0;
        int currentSteps = 0;
        BufferedReader console = new BufferedReader( new InputStreamReader(System.in));
        while(currentRoom!=numberOfRooms){
            try{
                String instruction = console.readLine();
                String instructionName = getName(instruction);
                if(instructionName.equals("Move")){
                    currentSteps++;
                    System.out.println(instruction.substring(
                            instruction.indexOf(':') + 1));
                    int roomToMove = Integer.parseInt(instruction.substring(
                            instruction.indexOf(':') + 1));
                    int room, position;
                    position = list[currentRoom]; 
                    boolean verification = false;
                    while(position!=0){
                        room = peak[position];
                        if(room == roomToMove){
                            currentGold += goldByConnection[position];
                            verification = true;
                        }
                        position = next[position];
                    }
                    if(verification == true){
                        currentRoom = roomToMove;
                        System.out.println("You are in room " + currentRoom+ " and spent "+currentGold + " gold");
                        
                    }else{
                        System.out.println("No room found");
                    }
                    
                } else
                if(instructionName.equals("List")){
                    int room, position;
                    position = list[currentRoom];
                    boolean verification = false;
                    while(position!=0){
                        room = peak[position];
                        System.out.print(room + " ");
                        System.out.print(goldByConnection[position]);
                        System.out.println();
                        position = next[position];
                    }
                    System.out.println();
                } else{
                    System.out.println("Command not found");
                }
            }catch(IOException e){
                System.out.println("Problem reading from keyboard");
            }
        }
        System.out.println("Congratulations!");
        System.out.println("You won with a total of "+ currentGold+ " gold in "+ 
                currentSteps + "steps!");
        System.out.println("Best possible score would be"+ distance[numberOfRooms]
                +" gold in "+ steps[numberOfRooms] + "steps!");
    }
    
    public static String getName(String  instruction){
        if(instruction.length() > 3){
        String name = instruction.substring(0, 4);
        System.out.println(name);
        return name;
        }
        else{
         return "nonsense";
        }
        
    }
    
    public static void main(String[] args) {
        createLabirinth("C:\\Users\\alexa\\Desktop\\game.txt");
        init();
        bellmanFord();
        
        if(negativ == true){
            System.out.println("Couldn't found solution, negative cycle");
        }
        playGame();
    }
    
}
