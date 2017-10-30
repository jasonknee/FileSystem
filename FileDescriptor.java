public class FileDescriptor {
    LogicalDisk logicalDisk;
    public byte[] blockPointers;
    public int fileLength;

    // public FileDescriptor(byte[] arrayOfBlockPointers, int length) {
    //     blockPointers = arrayOfBlockPointers;
    //     fileLength = length;
    // }

    public FileDescriptor(LogicalDisk disk) {
        logicalDisk = disk;
        blockPointers = new byte[3];
        fileLength = -1;
    }

    void saveToFileDescriptorIndex(int index) {
        System.out.printf("==> void FileDescriptor.saveToFileDescriptorIndex(int index = %d);\n", index);        
        logicalDisk.disk.pack(fileLength, index);
        // ALLOCATE BLOCK INDEX
    }
}