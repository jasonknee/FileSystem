public class FileTableEntry {
    public byte[] bufferData;
    public int fileLength;
    public int positionInFile;
    public int fileDescriptorIndex;
    public int fileTableIndex;
    public boolean available;

    public FileTableEntry(byte[] data, int length, int pos, int i, int ftI) {
        bufferData = data;
        fileLength = length;
        positionInFile = pos;
        fileDescriptorIndex = i;
        fileTableIndex = ftI;
        available = false;
    }

    public void init(byte[] data, int length, int pos, int i, int ftI) {
        bufferData = data;
        fileLength = length;
        positionInFile = pos;
        fileDescriptorIndex = i;
        fileTableIndex = ftI;
        available = false;
    }

    public FileTableEntry(int index) {
        bufferData = null;
        fileLength = -1;
        positionInFile = 0;
        fileDescriptorIndex = -1;
        fileTableIndex = index;
        available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void moveToPosition(int position) {}
    public byte[] readFile(int count) { return new byte[64]; };   
    public int writeFile(char c, int count) { return 0; }
}