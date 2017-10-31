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
        // System.out.printf("==> int Directory.findFileDescriptorIndexOfFile(String filename = %s);\n", filename);

        byte[] nameInBytes = filename.getBytes();
        int[] directoryBlockPointers = getDirectoryBlockNumbers();

        for (int i = 0; i < directoryBlockPointers.length; i++) {
            if (directoryBlockPointers[i] != 0) {
                for (int j = 0; j < 64; j = j + 8) {
                    
                    if (filename.equals(logicalDisk.disk.unpackString(directoryBlockPointers[i]*64+j))) {
                        int fileIndex = logicalDisk.disk.unpack(directoryBlockPointers[i]*64+j+4);   
                        return fileIndex;
                    }
                }
            }
        }
        return -1;
    }

    public int getFreeFileDescriptorIndex() {
        // System.out.printf("==> int Directory.getFreeFileDescriptorIndex();\n");
        for (int i=1; i<8; i++) {
            for (int j=0; j<64; j=j+16) {
                if (i == 1 && j == 0) {
                } 
                else {
                    int lengthOfFile = logicalDisk.getIntOfBlockWithIndex(i, j);
                    if (lengthOfFile == 0) {
                        return i*64+j;
                    }
                }
            }
        }

        return -1;
    }

    public int createNewFile(String filename, int index) {
        // System.out.printf("==> int Directory.createNewFile(String filename = %s, int index = %d);\n", filename, index);
        
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

    public void trashFile(String filename) {
        // System.out.printf("==> int Directory.trashFile(String filename = %s);\n", filename);
        try {
            int fileDescriptorIndex = removeFileFromDirectory(filename);
            if (fileDescriptorIndex != -1) {
                removeFileDescriptorAt(fileDescriptorIndex);                
            }
        }
        catch (Exception e) {
            System.out.println(e);
            // return -1;
        }
    }

    int insertFileIntoDirectory(String filename, int index) {
        // System.out.printf("==> int Directory.insertFileIntoDirectory(String filename = %s, int index = %d);\n", filename, index);        
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

    int removeFileFromDirectory(String filename) {
        // System.out.printf("==> int Directory.removeFileFromDirectory(String filename = %s);\n", filename);        
        int[] directoryBlockNumbers = getDirectoryBlockNumbers();
        for (int i=0; i<directoryBlockNumbers.length; i++) {

            for (int j=0; j<64; j=j+8) {
                int fileDescriptorIndex = logicalDisk.disk.unpack(64*directoryBlockNumbers[i]+j+4);
                if (filename.equals(logicalDisk.disk.unpackString(64*directoryBlockNumbers[i]+j))) {
                    logicalDisk.disk.pack(0, 64*directoryBlockNumbers[i]+j);
                    logicalDisk.disk.pack(0, 64*directoryBlockNumbers[i]+j+4);
                    return fileDescriptorIndex;
                }
            }
        }
        return -1;
    }

    void newFileDescriptorAt(int index) {
        // System.out.printf("==> int Directory.newFileDescriptorAt(int index = %d);\n", index);
        FileDescriptor newFileDescriptor = new FileDescriptor(logicalDisk);
        newFileDescriptor.saveToFileDescriptorIndex(index);
    }

    void removeFileDescriptorAt(int index) {
        // System.out.printf("==> int Directory.newFileDescriptorAt(int index = %d);\n", index);
        FileDescriptor fileDescriptorToDelete = new FileDescriptor(logicalDisk);
        fileDescriptorToDelete.deleteFileDescriptorAt(index);
    }

    public int getBlockIndexOfFile(int index, int fdBlockIndex) {
        // System.out.printf("==> void FileTable.getBlockIndexOfFile(int index = %d, int fdBlockIndex = %d);\n", index, fdBlockIndex);
        int blockIndex = logicalDisk.disk.unpack(index+4 + (4*fdBlockIndex));
        if (blockIndex != 0) {
            return blockIndex;
        }
        int nextAvailableBlock = logicalDisk.getNextAvailableBlock();
        if (nextAvailableBlock != -1) {
            logicalDisk.disk.pack(nextAvailableBlock, index+4 + (4*fdBlockIndex));     
            return nextAvailableBlock;
        }
        return -1;
    }

    public int getLengthOfFile(int index) {
        return 0;
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