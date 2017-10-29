public class Directory {
    public LogicalDisk logicalDisk;

    /* Directory entries */
    /* every pair store file descriptor index and file name. */
    // dir_entries[0] = 'abc\n'
    // dir_entries[1] = i;
    private int dir_entries[];

    public Directory(LogicalDisk disk) {
        logicalDisk = disk;
    }

    String[] arrayOfFileNames() {
        String[] array = new String[46];
        return array;
    }

    public int getFreeBlock() { return 0; }
    public int createNewFile(String filename, int index) { return 0;}
    public int getBlockIndexOfFile(int index, int blockNum) { return 0; }
    public int getLengthOfFile(int index) { return 0; }
    public int findFileDescriptorIndexOfFile(String filename) { return 0;}    
    public void trashFile(String filename) {}    
    public void updateLengthOfFileDescriptor(int index) {}
}