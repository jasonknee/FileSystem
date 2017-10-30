import java.util.*;

public class Directory {

    public LogicalDisk logicalDisk;

    /* Directory entries */
    /* every pair store file descriptor index and file name. */
    // dir_entries[0] = 'abc\0'
    // dir_entries[1] = i;
    private int[] file_indexes;
    private String[] file_names;

    public Directory(LogicalDisk disk) {
        logicalDisk = disk;
        file_indexes = new int[24];
        file_names = new String[24];
    }

    String[] arrayOfFileNames() {
        String[] array = new String[46];
        return array;
    }

    public int findFileDescriptorIndexOfFile(String filename) {
        System.out.printf("==> int Directory.findFileDescriptorIndexOfFile(String filename = %s);\n", filename);

        byte[] nameInBytes = filename.getBytes();
        int[] directoryBlockPointers = getDirectoryBlockNumbers();

        for (int i = 0; i < directoryBlockPointers.length; i++) {
            if (directoryBlockPointers[i] != 0) {
                byte[] block = logicalDisk.readBlock(directoryBlockPointers[i]);

                for (int j = 0; j < block.length; j = j + 8) {
                    byte[] directoryEntryFileName = Arrays.copyOfRange(block, j, j + 4); // ONE FILE DIRECTORY ENTRY

                    if ((Arrays.equals(nameInBytes, directoryEntryFileName))) { // COMPARE USER INPUT AND FILENAME OF DIRECTORY ENTRY
                        return logicalDisk.getIntOfBlockWithIndex(i, j);
                    }
                }
            }
        }
        return -1;
    }

    public int getFreeFileDescriptorIndex() {
        System.out.printf("==> int Directory.getFreeFileDescriptorIndex();\n");
        for (int i=1; i<8; i++) {
            for (int j=0; j<64; j=j+16) {
                if (i == 1 && j == 0) {
                } 
                else {
                    int lengthOfFile = logicalDisk.getIntOfBlockWithIndex(i, j);
                    System.out.printf("i: %d, j: %d", i, j);                    
                    System.out.println(lengthOfFile);
                    if (lengthOfFile == 0) {
                        return i*64+j;
                    }
                }
            }
        }

        return -1;
    }

    public int createNewFile(String filename, int index) {
        System.out.printf("==> int Directory.createNewFile(String filename = %s, int index = %d);\n", filename, index);
        
        try {
            if (insertFileIntoDirectory(filename, index) == -1) {
                return -1;
            } 

            newFileDescriptorAt(index);
            return 0;
        }
        catch (Exception e) {
            return -1;
        }
    }

    int insertFileIntoDirectory(String filename, int index) {
        System.out.printf("==> int Directory.insertFileIntoDirectory(String filename = %s, int index = %d);\n", filename, index);        
        int[] directoryBlockNumbers = getDirectoryBlockNumbers();
        for (int i=0; i<directoryBlockNumbers.length; i++) {

            for (int j=0; j<64; j=j+8) {
                if(logicalDisk.disk.unpack(64*directoryBlockNumbers[i]+j+4) == 0) {
                    logicalDisk.disk.packString(filename, 64*directoryBlockNumbers[i]+j);
                    logicalDisk.disk.pack(index, 64*directoryBlockNumbers[i]+j+4);
                    return 0;
                }
            }
        }
        return -1;
    }

    void newFileDescriptorAt(int index) {
        System.out.printf("==> int Directory.newFileDescriptorAt(int index = %d);\n", index);
        FileDescriptor newFileDescriptor = new FileDescriptor(logicalDisk);
        newFileDescriptor.saveToFileDescriptorIndex(index);
    }

    public int getBlockIndexOfFile(int index, int blockNum) {
        return 0;
    }

    public int getLengthOfFile(int index) {
        return 0;
    }

    public void trashFile(String filename) {
    }

    public void updateLengthOfFileDescriptor(int index) {
    }

    private byte[] getDirectoryBlock() {
        return logicalDisk.readBlock(1);
    }

    private int[] getDirectoryBlockNumbers() {
        int[] blockNumbers = new int[3];
        blockNumbers[0] = 61;
        blockNumbers[1] = 62;
        blockNumbers[2] = 63;
        
        // blockNumbers[0] = logicalDisk.getIntOfBlockWithIndex(1, 4);
        // blockNumbers[1] = logicalDisk.getIntOfBlockWithIndex(1, 8);
        // blockNumbers[2] = logicalDisk.getIntOfBlockWithIndex(1, 12);
        return blockNumbers;
    }
}