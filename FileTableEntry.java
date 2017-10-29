public class FileTableEntry {
    public byte[] bufferData;
    public int fileLength;
    public int position;
    public int index;
    public FileTableEntry(byte[] data, int length, int pos, int i) {
        bufferData = data;
        fileLength = length;
        position = pos;
        index = i;
    }
}