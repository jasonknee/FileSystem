import java.util.Scanner;
import java.util.Arrays;
import java.io.*;
import java.nio.file.Files;

// ...

public class Exec {
    public static FileSystem fs;

    public Exec() 
    {
    }

    public static String[] stringParser(String str) {
        String[] splitStr = str.split("\\s+");
        return splitStr;
    }

    public static void executeCommand(String[] inputs) {
        String example_cmd = "";
        String cmd = inputs[0];
        String[] args = Arrays.copyOfRange(inputs, 1, inputs.length);

        try {
            if (cmd.equals("cr")) {
                example_cmd = "cr <name>";
                String filename = args[0];

                fs.create(filename);
            } else if (cmd.equals("de")) {
                example_cmd = "de <name>";
                String filename = args[0];

                fs.destroy(filename);
            } else if (cmd.equals("op")) {
                example_cmd = "op <name>";
                String filename = args[0];

                fs.open(filename);
            } else if (cmd.equals("cl")) {
                example_cmd = "cl <index>";
                int index = Integer.valueOf(args[0])-1;

                fs.close(index);
            } else if (cmd.equals("rd")) {
                example_cmd = "rd <index> <count>";
                int index = Integer.valueOf(args[0]) - 1;
                int count = Integer.valueOf(args[1]);

                fs.read(index, count);
            } else if (cmd.equals("wr")) {
                example_cmd = "wr <index> <char> <count>";
                int index = Integer.valueOf(args[0]) - 1;
                char c = args[1].charAt(0);
                int count = Integer.valueOf(args[2]);

                fs.write(index, c, count);
            } else if (cmd.equals("sk")) {
                example_cmd = "sk <index> <pos>";
                int index = Integer.valueOf(args[0]) - 1;
                int pos = Integer.valueOf(args[1]);

                fs.lseek(index, pos);
            } else if (cmd.equals("dr")) {
                example_cmd = "dr";

                fs.directory();
            } else if (cmd.equals("in")) {
                example_cmd = "in <disk_cont.txt>";
                String filename = args.length > 0 ? args[0] : "";

                fs.init(filename);
            } else if (cmd.equals("sv")) {
                example_cmd = "sv <disk_cont.txt>";
                String filename = args[0];

                fs.save(filename);
            } else if (cmd.equals("")) {
                System.out.println("");
            } else {
                System.out.println("/*************************************/\n" + "       ERROR: NO COMMAND ENTERED \n"
                        + "       EXAMPLE CMD: init\n" + "/*************************************/\n");
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("/*************************************/\n" + "       ERROR: BAD ARGS \n"
                    + "       EXAMPLE ARGS: " + example_cmd + "\n" + "/*************************************/\n");
        }
    }

    public static void main(String[] args) {

        try {
            fs = new FileSystem();

            Scanner scanner = new Scanner(System.in);
            System.out.println("FS Executor (by Jason)\n");
            System.out.println("Enter file name: ");
        
            String fileName = scanner.nextLine();
            File file = new File(fileName);
            String absPath = file.getAbsolutePath();
            String path = absPath.substring(0,absPath.lastIndexOf(File.separator));;

            System.out.println(path);
            PrintStream psr = new PrintStream(new FileOutputStream(path+"/77653453.txt", false));
            System.setOut(psr);

        
            Files.lines(file.toPath())
                .map(d -> d.trim())
                .forEach(d -> executeCommand(stringParser(d)));
        }

        catch (Exception e) {

        }

        // scanner.close();
    }
}