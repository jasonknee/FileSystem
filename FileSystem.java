import java.util.*;

public class FileSystem {
    private final int BLOCK_SIZE = 64;

    public LogicalDisk logicalDisk;
    public BitMap bitMap;
    public Directory directory;
    public FileTable fileTable;

    public FileSystem() {
        logicalDisk = new LogicalDisk(BLOCK_SIZE);
        bitMap = new BitMap(logicalDisk);
        directory = new Directory(logicalDisk);
        fileTable = new FileTable(logicalDisk, directory);
        fileTable.alloc();
    }

    void dealloc() {
        logicalDisk = null;
        bitMap = null;
        directory = null;
        fileTable = null;
    }

    void alloc() {
        logicalDisk = new LogicalDisk(BLOCK_SIZE);
        bitMap = new BitMap(logicalDisk);
        directory = new Directory(logicalDisk);
        fileTable = new FileTable(logicalDisk, directory);
        fileTable.alloc();
    }
    
    int create(String filename) {
        // System.out.printf("=> void create(char[] filename = %s);\n", filename);

        int fileDescriptorIndex = directory.findFileDescriptorIndexOfFile(filename);
        // System.out.printf("===== OUTPUT: fileDescriptorIndex = %d\n", fileDescriptorIndex);        
        if (fileDescriptorIndex == -1) { // IF WE DON'T FIND A FILE

            fileDescriptorIndex = directory.getFreeFileDescriptorIndex();
            // System.out.printf("===== OUTPUT: fileDescriptorIndex = %d\n", fileDescriptorIndex);        
            if (fileDescriptorIndex != -1) { // IF WE GET A FREE FILE DESCRIPTOR BLOCK

                if (directory.createNewFile(filename, fileDescriptorIndex) == 0) {
                    System.out.printf("%s created\n", filename);
                    return 0;
                }
            }
        }

        System.out.println("error");
        return -1;
    }

    // • Find the file descriptor by searching the directory
    // • Remove the directory entry
    // • Update the bitmap to reflect the freed blocks
    // • Free the file descriptor
    // • Return status
    int destroy(String filename) {
        // System.out.printf("=> void destroy(char[] filename = %s);\n", filename);

        try {
            int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
            if (fileDescriptorBlockIndex == -1) {
                System.out.println("error");            
                return -1;
            }
            System.out.printf("%s is destroyed\n", filename);
            directory.trashFile(filename);
        }
        catch (Exception e) {
            System.out.println("error");
        }
        return 0;        
    }

    // • Search the directory to find the index of the file descriptor
    // • Allocate a free OFT entry (if possible)
    // • Fill in the current position (zero) and the file descriptor index
    // • Read the first block of the file into the buffer (read-ahead)
    // • Return the OFT index (or error status)
    int open(String filename) {
        // System.out.printf("=> void open(char[] filename = %s);\n", filename);

        if (!fileTable.isFree()) {
            System.out.println("error");            
            return -1; // NO OPEN FILE TABLES
        }

        if (fileTable.isFileOpen(filename)) {
            System.out.println("error");            
            return -1; // FILE ALREADY OPEN IN FILE TABLE
        }

        int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        if (fileDescriptorBlockIndex == -1) {
            System.out.println("error");                        
            return -1; // DID NOT FIND FILE
        }

        int fileLength = directory.getLengthOfFile(fileDescriptorBlockIndex);
        int blockIndex = directory.getBlockIndexOfFile(fileDescriptorBlockIndex, 0); // GET BLOCK INDEX
        byte[] blockData = logicalDisk.readBlock(blockIndex);
        // System.out.printf("File Length %d\n", fileLength);
        int openFileTableIndex = fileTable.allocateEntry(blockData, filename, fileLength, 0, fileDescriptorBlockIndex);
        

        if (openFileTableIndex == -1) {
            System.out.println("error");            
            return -1;            
        }

        System.out.printf("%s opened %d\n", filename, openFileTableIndex+1);
                        
        return openFileTableIndex;
    }

    // • Write the buffer to disk
    // • Update file length in descriptor
    // • Free the OFT entry
    // • Return status
    int close(int fileTableIndex) {
        // System.out.printf("=> int close(int fileTableIndex = %s);\n", fileTableIndex);        

        try {
            FileTableEntry fileTableEntry = fileTable.getFileTableEntry(fileTableIndex);
            fileTable.writeToDisk(fileTableIndex);
            fileTableEntry.dealloc(fileTableIndex);
    
            directory.updateLengthOfFileDescriptor(fileTableEntry.fileDescriptorIndex);
            fileTable.freeEntry(fileTableEntry.fileTableIndex);
            System.out.printf("%d closed\n", fileTableIndex + 1);
        }
        catch (Exception e) {
            System.out.println("error");
        }
        
        return 0;
    }


    // 1. Compute the position within the read/write buffer that corresponds to the current
    // position within the file (i.e., file length modulo buffer length)
    // 2. Start copying bytes from the buffer into the specified main memory location until
    // one of the following happens:
    //      (a) the desired count or the end of the file is reached; in this case, update current
    //          position and return status
    //      (b) the end of the buffer is reached; in this case,
    //              • write the buffer into the appropriate block on disk (if modified),
    //              • read the next sequential block from the disk into the buffer;
    //              • continue with step 2.
    int read(int fileTableIndex, int count) {
        // logicalDisk.printDisk();
        // System.out.printf("\n\n");
        // System.out.printf("=> int read(int fileTableIndex = %s, int count, %d);\n", fileTableIndex, count);                
        int bytesRead = fileTable.readFile(fileTableIndex, count);
        // System.out.printf("bytes read: %d", bytesRead);
        return bytesRead;
    }

    // • compute position in the r/w buffer
    // • copy from memory into buffer until (LOOP)
    //      • desired count or end of file is reached:
    //              update current pos, return status
    //      • end of buffer is reached
    //          • if block doesn't exist yet (file is expanding)
    //                      - allocate new block (search and update bitmap)
    //                      - update file descriptor with new block number
    //          • write the buffer to disk block
    //          • JUMP TO (LOOP)
    //  • update file length in descriptor
    int write(int fileTableIndex, char character, int count) {
        // System.out.printf("=> int write(int fileTableIndex = %d, char c = %s, int count = %d);\n", fileTableIndex, character, count);
        try { 
            int bytesWritten = fileTable.writeCharsToFile(fileTableIndex, character, count);
            System.out.printf("%d bytes written\n", bytesWritten);        
            // logicalDisk.printDisk();
        }             
        catch (Exception e) {
            System.out.println("error");
        }

        return 0;
    }

    int lseek(int fileTableIndex, int position) {
        // System.out.printf("=> int lseek(int fileTableIndex = %s, int position, %d);\n", fileTableIndex, position);                
        
        boolean success = fileTable.moveToPosition(fileTableIndex, position);

        if (success) {
            System.out.printf("position is %d\n", position);                            
            return position;
        }
        System.out.printf("error\n");                                    
        return -1;
    }

    void directory() {
        // System.out.printf("=> String[] directory();\n");    
        try {
            List<String> arrayOfFileNames;
            arrayOfFileNames = directory.arrayOfFileNames();
            for (String fileName : arrayOfFileNames) {
                System.out.printf("%s ", fileName);
            } 
            System.out.printf("\n");
        }            
        catch (Exception e) {
            System.out.println("error");
        }
       
    }

    String init(String filename) {
        // System.out.printf("=> void int(String filename = %s);\n", filename);      
        dealloc();
        alloc();

        if (FileStream.fileExists(filename)) {
            byte[] fileBlock = FileStream.getFileAsByteArray(filename);
            logicalDisk.init(fileBlock);
            // logicalDisk.printDisk();
            // System.out.printf("\n\n");
            System.out.println("disk restored");            
            return "disk restored";
        }
        System.out.println("disk initialized");
        return "disk initialized";
    }

    String save(String filename) {
        // System.out.printf("=> void save(String filename = %s);\n", filename);        
        try {
            byte[] disk = logicalDisk.fullDiskAsByteArray();
            FileStream.write(filename, disk);
            System.out.println("disk saved");
            return "disk saved";
        }
        catch (Exception e) {
            System.out.println("error");
            return "error";
        }
    }

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        String filename = "char";
        fs.create(filename);
    }
}