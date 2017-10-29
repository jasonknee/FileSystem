import java.util.Scanner;
import java.util.Arrays;

public class Driver 
{
    public static FileSystem fs;

    public Driver() 
    {
    }
    
    public static String[] stringParser(String str) 
    {
        String[] splitStr = str.split("\\s+");
        return splitStr;
    }

    public static void executeCommand(String[] inputs) 
    {
        String example_cmd = "";
        String cmd = inputs[0];            
        String[] args = Arrays.copyOfRange(inputs, 1, inputs.length);
        
        try {
            if (cmd.equals("cr")) {
                example_cmd = "cr <name>";
                fs.create(args[0]);
            } else if (cmd.equals("de")) {
                example_cmd = "de <name>";                
                fs.destroy(args[0]);
            } else if (cmd.equals("op")) {
                example_cmd = "op <name>";                
                fs.open(args[0]);
            } else if (cmd.equals("cl")) {
                example_cmd = "cl <index>";
                fs.close(args[0]);
            } else if (cmd.equals("rd")) {
                example_cmd = "rd <index> <count>";
                fs.read(args[0], args[1], args[2]);
            } else if (cmd.equals("wr")) {
                example_cmd = "wr <index> <char> <count>";                
                fs.write(args[0], args[1], args[2]);
            } else if (cmd.equals("sk")) {
                example_cmd = "sk <index> <pos>";                
                fs.lseek(args[0], args[1]);
            } else if (cmd.equals("dr")) {
                example_cmd = "dr";                
                fs.directory();
            } else if (cmd.equals("in")) {
                example_cmd = "in <disk_cont.txt>";                
                fs.init(args[0]);
            } else if (cmd.equals("sv")) {
                example_cmd = "sv <disk_cont.txt>";                                
                fs.save(args[0]);
            } else {
                System.out.println("/*************************************/\n"
                + "       ERROR: NO COMMAND ENTERED \n"
                + "       EXAMPLE CMD: init\n" 
                + "/*************************************/\n");
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("/*************************************/\n"
                             + "       ERROR: BAD ARGS \n"
                             + "       EXAMPLE ARGS: " + example_cmd + "\n" 
                             + "/*************************************/\n");
        }
    }

    public static void main(String[] args) 
    {
        fs = new FileSystem();

        String[] inputs;

        Scanner scanner = new Scanner(System.in);
        System.out.println("FS (by Jason)");
                
        do {
            System.out.println("Enter Your Command: \n");

            inputs = stringParser(scanner.nextLine());
            executeCommand(inputs);

            // break;
        } while (true);
        // scanner.close();
    }
}