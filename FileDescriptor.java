public class FileDescriptor {
    public byte[] blockPointers;
    public int fileLength;

    public FileDescriptor(byte[] arrayOfBlockPointers, int length) {
        blockPointers = arrayOfBlockPointers;
        fileLength = length;
    }
}