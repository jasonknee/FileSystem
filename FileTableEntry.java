public class FileTableEntry {
    public byte[] bufferData;
    public int fileLength;
    public int positionInFile;
    public int fileDescriptorIndex;
    public int fileTableIndex;

    public FileTableEntry(byte[] data, int length, int pos, int i, int ftI) {
        bufferData = data;
        fileLength = length;
        positionInFile = pos;
        fileDescriptorIndex = i;
        fileTableIndex = ftI;
    }
}