public class FileTableEntry {
    public byte[] bufferData;
    public String fileName;
    public int fileLength;
    public int positionInFile;
    public int fileDescriptorIndex;
    public int fileTableIndex;
    public boolean available;

    public FileTableEntry(byte[] data, String name, int length, int pos, int i, int ftI) {
        bufferData = data;
        fileName = name;
        fileLength = length;
        positionInFile = pos;
        fileDescriptorIndex = i;
        fileTableIndex = ftI;
        available = false;
    }

    public void init(byte[] data, String name, int length, int pos, int i, int ftI) {
        bufferData = data;
        fileName = name;
        fileLength = length;
        positionInFile = pos;
        fileDescriptorIndex = i;
        fileTableIndex = ftI;
        available = false;
    }

    public FileTableEntry(int index) {
        bufferData = null;
        fileName = "";
        fileLength = -1;
        positionInFile = 0;
        fileDescriptorIndex = -1;
        fileTableIndex = index;
        available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getFileName() {
        return fileName;
    }

    public void moveToPosition(int position) {}
    public byte[] readFile(int count) { return new byte[64]; };   
    public int writeFile(char c, int count) { return 0; }
}