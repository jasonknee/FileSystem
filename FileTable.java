public class FileTable {
    FileTableEntry[] fileTable;
    LogicalDisk logicalDisk;

    public FileTable(LogicalDisk disk) {
        fileTable = new FileTableEntry[3];
        logicalDisk = disk;
    }

    public void alloc() {
        for (int i=0; i<fileTable.length; i++) {
            fileTable[i] = new FileTableEntry(i);
        }
    }

    public int insert(String filename, int index) {
        System.out.printf("==> void FileTable.insert(String filename = %s, int index = %d);\n", filename, index);

        int freeTableIndex = getFreeTableIndex();
        if (freeTableIndex != -1) {
        }
        return -1;
    }

    public int getFreeTableIndex() { 
        for (int i=0; i<fileTable.length; i++) {
            if (fileTable[i].fileLength == -1) {
                return fileTable[i].fileTableIndex;
            }
        }
        return -1;
     }
    

    public FileTableEntry allocateEntry(byte[] data, int index, int length, int position) { return new FileTableEntry(null,0,0,0,0); }
    public FileTableEntry getFileTableEntry(int index) { return new FileTableEntry(null,0,0,0,0); }
    public FileTableEntry writeToDisk(int index) { return new FileTableEntry(null,0,0,0,0); }
    public int findFileDescriptorIndexOfFile(int index) { return 0; }
    public int getLengthOfFile(int index) { return 0; }
    public int getBlockIndexOfFile(int index) { return 0; }
    public void freeEntry(int index) {}
    public boolean isFree() { return true; }
    
}