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

    void read(String index, String mem_area, String count) {
    }

    void write(String index, String mem_area, String count) {
    }

    void lseek(String index, String pos) {
    }

    void directory() {
    }

    void init(String file_name) {
    }

    void save(String file_name) {
    }

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        fs.create("hey");
    }
}