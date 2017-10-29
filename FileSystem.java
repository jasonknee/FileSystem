public class FileSystem {
    private final int BLOCK_SIZE = 64;

    public LogicalDisk logicaldisk;
    public BitMap bitMap;
    public Directory directory;
    public FileTable fileTable;

    public FileSystem() {
        logicalDisk = new LogicalDisk(BLOCK_SIZE);
        bitMap = new BitMap(logicalDisk);
        directory = new Directory(logicaldisk);
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
        System.out.printf("void create(%s);\n", filename);
        int fileDescriptorBlockIndex;

        fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        if (fileDescriptorBlockIndex == -1) { // IF WE DON'T FIND A FILE

            fileDescriptorBlockIndex = directory.getFreeBlock();
            if (fileDescriptorBlockIndex != -1) { // IF WE GET A FREE FILE DESCRIPTOR BLOCK

                fileDescriptorBlockIndex = directory.createNewFile(filename, fileDescriptorBlockIndex);
                filetable.insert(filename, fileDescriptorBlockIndex);
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
        System.out.printf("void destroy(%s);\n", filename);

        int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        if (fileDescriptorBlockIndex == -1) {
            return -1;
        }

        directory.trashFile(filename);
        bitMap.freeBlockIndex(blockIndex);
        return 0;

    }

    // • Search the directory to find the index of the file descriptor
    // • Allocate a free OFT entry (if possible)
    // • Fill in the current position (zero) and the file descriptor index
    // • Read the first block of the file into the buffer (read-ahead)
    // • Return the OFT index (or error status)
    int open(String filename) {
        System.out.printf("void open(%s);\n", filename);

        if (!filetable.isFree()) {
            return -1; // NO OPEN FILE TABLES
        }

        int fileDescriptorBlockIndex = directory.findFileDescriptorIndexOfFile(filename);
        if (fileDescriptorBlockIndex == -1) {
            return -1; // DID NOT FIND FILE
        }

        int fileLength = directory.getLengthOfFile(fileDescriptorBlockIndex);
        int blockIndex = directory.getBlockIndexOfFile(fileDescriptorIndex, 0);
        byte[] blockData = logicalDisk.readBlock(blockIndex);
        FileTableEntry fileTableEntry = fileTable.allocateEntry(blockData, fileDescriptorBlockIndex, fileLength, 0);

        return fileTableEntry.fileTableIndex;
    }

    // • Write the buffer to disk
    // • Update file length in descriptor
    // • Free the OFT entry
    // • Return status
    int close(String fileTableIndex) {
        FileTableEntry fileTableEntry = filetable.writeToDisk(fileTableIndex);

        if (fileTableEntry == null) {
            return -1;
        }

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
    int read(int fileTableIndex, int count) {
        byte[] data;

        FileTableEntry fileTableEntry = filetable.getFileTableEntry(fileTableIndex);

        if (fileTableEntry == null) {
            return -1;
        }

        byte[] file = fileTableEntry.readFile(fileTableIndex, count);

        return 0;
    }

    void write(String index, String mem_area, String count) {}

    void lseek(String index, String pos) {}

    void directory() {}

    void init(String file_name) {}

    void save(String file_name) {}

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        fs.create("hey");
    }
}