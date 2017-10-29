public class FileTableEntry {
    public byte[] bufferData;
    public int fileLength;
    public int position;
    public int fileDescriptorIndex;
    public int fileTableIndex;

    public FileTableEntry(byte[] data, int length, int pos, int i, int ftI) {
        bufferData = data;
        fileLength = length;
        position = pos;
        fileDescriptorIndex = i;
        fileTableIndex = ftI;
    }
}