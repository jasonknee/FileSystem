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
            // System.out.printf("%d\n", directoryBlockPointers[i]);
            byte[] block = logicalDisk.readBlock(directoryBlockPointers[i]);

            for (int j = 0; j < block.length; j = j + 8) {
                byte[] directoryEntries = Arrays.copyOfRange(block, j, j + 4);
                // System.out.printf("%d %d %d %d %d %d %d %d\n", directoryEntries[0], directoryEntries[1],
                //         directoryEntries[2], directoryEntries[3], block[j + 3], block[j + 4], block[j + 5],
                //         block[j + 6]);

                if ((Arrays.equals(nameInBytes, directoryEntries))) {
                    return logicalDisk.getIntOfBlockWithIndex(i, j);
                }
            }
        }
        return -1;
    }

    public int getFreeBlock() {
        System.out.printf("==> int Directory.getFreeBlock();\n");
        return 0;
    }

    public int createNewFile(String filename, int index) {
        System.out.printf("==> int Directory.createNewFile(String filename = %s, int index = %d);\n", filename, index);
        return 0;
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
        blockNumbers[0] = logicalDisk.getIntOfBlockWithIndex(1, 4);
        blockNumbers[1] = logicalDisk.getIntOfBlockWithIndex(1, 8);
        blockNumbers[2] = logicalDisk.getIntOfBlockWithIndex(1, 12);
        return blockNumbers;
    }
}