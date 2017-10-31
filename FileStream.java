import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
public class FileStream {
    static final int DISK_SIZE = 64 * 64;

    public FileStream() {
    }

    static byte[] getFileAsByteArray(String filename) {
        // System.out.printf("=> byte[] FileStream.getFileAsByteArray(string filename = %s);\n", filename);

        byte[] fileDisk = new byte[DISK_SIZE];

        try {
            byte[] array = Files.readAllBytes(new File(filename).toPath());
            return array;
        } catch (Exception e) {
            System.out.println("error");
            System.exit(0);
        }

        return fileDisk;
    }

    static int write(String filename, byte[] disk) {
        try {
            File f = new File(filename);        
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(disk);
            fos.close();
            return 0;
        }
        catch (Exception e) {
            return -1;
        }
    }

    static boolean fileExists(String filename) {
        try {
            File f = new File(filename);

            if (f.createNewFile()) {
                return false;
            } else {
                return true;
            }

        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }

    }

    static void createFile(String filename) {
    }

    public static void main(String[] args) {
        // FileStream fs = new FileStream();
        String filename = "test.txt";
        FileStream.getFileAsByteArray(filename);
    }
}