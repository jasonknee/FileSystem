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
        fileTable = new FileTable(directory);
    }

    // • Find a free file descriptor (read in and scan ldisk [0] through ldisk [k − 1])
    // • Find a free entry in the directory (this is done by rewinding the directory and
    //   reading it until a free slot is found; recall that the directory is treated just like any
    //   other file). At the same time, verify that the file does not already exists. If it does,
    //   return error status.
    // • Enter the symbolic file name and the descriptor index into the found directory entry
    // • Return status
    int create(String filename) {
        System.out.printf("=> void create(char[] filename = %s);\n", filename);

        int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        System.out.printf("FileDescriptorIndex = %d", fileDescriptorBlockIndex);        
        if (fileDescriptorBlockIndex == -1) { // IF WE DON'T FIND A FILE

            fileDescriptorBlockIndex = directory.getFreeBlock();
            if (fileDescriptorBlockIndex != -1) { // IF WE GET A FREE FILE DESCRIPTOR BLOCK

                fileDescriptorBlockIndex = directory.createNewFile(filename, fileDescriptorBlockIndex);
                fileTable.insert(filename, fileDescriptorBlockIndex);
                return 0;
            }
        }

        return -1;
    }

    // • Find the file descriptor by searching the directory
    // • Remove the directory entry
    // • Update the bitmap to reflect the freed blocks
    // • Free the file descriptor
    // • Return status
    int destroy(String filename) {
        System.out.printf("=> void destroy(char[] filename = %s);\n", filename);

        int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        if (fileDescriptorBlockIndex == -1) {
            return -1;
        }

        directory.trashFile(filename);
        bitMap.freeBlockIndex(fileDescriptorBlockIndex);
        return 0;

    }

    // • Search the directory to find the index of the file descriptor
    // • Allocate a free OFT entry (if possible)
    // • Fill in the current position (zero) and the file descriptor index
    // • Read the first block of the file into the buffer (read-ahead)
    // • Return the OFT index (or error status)
    int open(String filename) {
        System.out.printf("=> void open(char[] filename = %s);\n", filename);

        if (!fileTable.isFree()) {
            return -1; // NO OPEN FILE TABLES
        }

        int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        if (fileDescriptorBlockIndex == -1) {
            return -1; // DID NOT FIND FILE
        }

        int fileLength = directory.getLengthOfFile(fileDescriptorBlockIndex);
        int blockIndex = directory.getBlockIndexOfFile(fileDescriptorBlockIndex, 0);
        byte[] blockData = logicalDisk.readBlock(blockIndex);
        FileTableEntry fileTableEntry = fileTable.allocateEntry(blockData, fileDescriptorBlockIndex, fileLength, 0);

        return fileTableEntry.fileTableIndex;
    }

    // • Write the buffer to disk
    // • Update file length in descriptor
    // • Free the OFT entry
    // • Return status
    int close(int fileTableIndex) {
        System.out.printf("=> int close(int fileTableIndex = %s);\n", fileTableIndex);                
        
        FileTableEntry fileTableEntry = fileTable.writeToDisk(fileTableIndex);

        if (fileTableEntry == null) { return -1; }

        directory.updateLengthOfFileDescriptor(fileTableEntry.fileDescriptorIndex);
        fileTable.freeEntry(fileTableEntry.fileTableIndex);
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
    byte[] read(int fileTableIndex, int count) {
        System.out.printf("=> int read(int fileTableIndex = %s, int count, %d);\n", fileTableIndex, count);                

        FileTableEntry fileTableEntry = fileTable.getFileTableEntry(fileTableIndex);

        if (fileTableEntry == null) { System.exit(0); }

        byte[] bytesRead = fileTableEntry.readFile(count);
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
        System.out.printf("=> int write(int fileTableIndex = %s, int count, %d);\n", fileTableIndex, count);                
        
        FileTableEntry fileTableEntry = fileTable.getFileTableEntry(fileTableIndex);
        if (fileTableEntry == null) { return -1; }

        int bytesWritten = fileTableEntry.writeFile(character, count);
        return bytesWritten;
    }

    int lseek(int fileTableIndex, int position) {
        System.out.printf("=> int lseek(int fileTableIndex = %s, int position, %d);\n", fileTableIndex, position);                
        
        FileTableEntry fileTableEntry = fileTable.getFileTableEntry(fileTableIndex);
        if (fileTableEntry == null) { return -1; }

        fileTableEntry.moveToPosition(position);
        return 0;
    }

    String[] directory() {
        System.out.printf("=> String[] directory();\n");                
        String[] arrayOfFileNames;
        arrayOfFileNames = directory.arrayOfFileNames();
        return arrayOfFileNames;
    }

    String init(String filename) {
        System.out.printf("=> void int(String filename = %s);\n", filename);        
        
        if (FileStream.fileExists(filename)) {
            byte[] fileBlock = FileStream.getFileAsByteArray(filename);
            logicalDisk.init(fileBlock);
            return "disk restored";
        }

        FileStream.createFile(filename);
        return "disk initialized";
    }

    String save(String filename) {
        System.out.printf("=> void save(String filename = %s);\n", filename);        
        try {
            byte[] disk = logicalDisk.fullDiskAsByteArray();
            FileStream.write(filename, disk);
            return "disk saved";
        }
        catch (Exception e) {
            return "error";
        }
    }

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        String filename = "char";
        fs.create(filename);
    }
}