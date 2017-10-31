import java.util.Scanner;
import java.io.File;

public class FileStream {
    static final int DISK_SIZE = 64*64;

    public FileStream() {}

    static byte[] getFileAsByteArray(String filename){
        // System.out.printf("=> byte[] FileStream.getFileAsByteArray(string filename = %s);\n", filename);
        
        byte[] fileDisk = new byte[DISK_SIZE];
        
        try {
            Scanner scanner = new Scanner(new File(filename));
            int i = 0;
            while (scanner.hasNextByte()) {
                fileDisk[i] = scanner.nextByte();
                i++;
            }
            scanner.close();
            return fileDisk;
        }
        catch (Exception e) {
            System.out.println("error");
            System.exit(0);
        }
        
        return fileDisk;
    }

    static int write(String filename, byte[] disk) { return 0; }
    static boolean fileExists(String filename) { return true; }
    static void createFile(String filename){}
    
    public static void main(String[] args) {
        // FileStream fs = new FileStream();
        String filename = "test.txt";
        FileStream.getFileAsByteArray(filename);
    }    
}