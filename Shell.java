import java.util.Scanner;
import java.util.Arrays;

public class Shell 
{
    static FileSystem filesys;

    public Shell() 
    {
       filesys = new FileSystem();
    }
    
    public static String[] stringParser(String str) 
    {
        String[] splitStr = str.split("\\s+");
        return splitStr;
    }

    public static void executeCommand(String[] inputs) 
    {
        String cmd = inputs[0];
        String example_cmd = "";
        String[] args = Arrays.copyOfRange(inputs, 1, inputs.length);

        try {
            if (cmd.equals("cr")) {
                example_cmd = "cr <name>";
                filesys.create(args[0]);
            } else if (cmd.equals("de")) {
                example_cmd = "de <name>";                
                filesys.destroy(args[0]);
            } else if (cmd.equals("op")) {
                example_cmd = "op <name>";                
                filesys.open(args[0]);
            } else if (cmd.equals("cl")) {
                example_cmd = "cl <index>";
                filesys.close(args[0]);
            } else if (cmd.equals("rd")) {
                example_cmd = "rd <index> <count>";
                filesys.read(args[0], args[1], args[2]);
            } else if (cmd.equals("wr")) {
                example_cmd = "wr <index> <char> <count>";                
                filesys.write(args[0], args[1], args[2]);
            } else if (cmd.equals("sk")) {
                example_cmd = "sk <index> <pos>";                
                filesys.lseek(args[0], args[1]);
            } else if (cmd.equals("dr")) {
                example_cmd = "dr";                
                filesys.directory();
            } else if (cmd.equals("in")) {
                example_cmd = "in <disk_cont.txt>";                
                filesys.init(args[0]);
            } else if (cmd.equals("sv")) {
                example_cmd = "sv <disk_cont.txt>";                                
                filesys.save(args[0]);
            } else {
                System.out.println("/*************************************/\n"
                + "       ERROR: NO COMMAND ENTERED \n"
                + "       EXAMPLE CMD: init\n" 
                + "/*************************************/\n");
            }
        } catch (Exception e) {
            System.out.println("/*************************************/\n"
                             + "       ERROR: BAD ARGS \n"
                             + "       EXAMPLE ARGS: " + example_cmd + "\n" 
                             + "/*************************************/\n");
        }
    }

    public static void main(String[] args) 
    {
        String input;
        String[] parsedInput;
        Scanner scanner = new Scanner(System.in);
        System.out.println("FS (by Jason)");
        
        do {
            System.out.println("Enter Your Command: \n");

            input = scanner.nextLine();
            parsedInput = stringParser(input);
            executeCommand(parsedInput);

            // break;
        } while (true);
        // scanner.close();
    }
}