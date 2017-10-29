import java.util.Scanner;

public class FileStream {
    private final int DISK_SIZE = 64*64;


    public FileStream() {}

    byte[] getFileAsByteArray(String filename){
        Scanner scanner = new Scanner(new File(filename));
        byte[] fileDisk = new fileDisk[DISK_SIZE];
        int i = 0;
        while (scanner.hasNextByte()) {
            fileDisk[i] = scanner.nextByte();
            i++;
        }
        scanner.close();
        return fileDisk;
    }

    public static void main(String[] args) {
        FileStream fs = new FileStream();
        fs.getFileAsByteArray("test.txt");
    }    
}