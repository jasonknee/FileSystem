public class FileTable {
    FileTable[] table;
    Directory directory;

    public FileTable(Directory dir) {
        // table = new Vector();
        directory = dir;
    }

    public int findFileDescriptorIndexOfFile(int index) { return 0; }
    public int getLengthOfFile(int index) { return 0; }
    public int getBlockIndexOfFile(int index) { return 0; }
    public void freeEntry(int index) {}
    public void insert(String name, int index) {}

    public boolean isFree() { return true; }

    public FileTableEntry allocateEntry(byte[] data, int index, int length, int position){
        return new FileTableEntry(null,0,0,0,0);        
    }

    public FileTableEntry getFileTableEntry(int index) {
        return new FileTableEntry(null,0,0,0,0);
    }
    public FileTableEntry writeToDisk(int index) {
        return new FileTableEntry(null,0,0,0,0);        
    }
}