public class FileDescriptor {
    LogicalDisk logicalDisk;
    public int[] blockPointers;
    public int fileLength;

    // public FileDescriptor(byte[] arrayOfBlockPointers, int length) {
    //     blockPointers = arrayOfBlockPointers;
    //     fileLength = length;
    // }

    public FileDescriptor(LogicalDisk disk) {
        logicalDisk = disk;
        blockPointers = new int[3];
        fileLength = -1;
    }

    void saveToFileDescriptorIndex(int index) {
        // System.out.printf("==> void FileDescriptor.saveToFileDescriptorIndex(int index = %d);\n", index);
        logicalDisk.disk.pack(fileLength, index);
        initNewBlock(index);
        // ALLOCATE BLOCK INDEX
    }

    void initNewBlock(int index) {
        int nextAvailableBlock = logicalDisk.getNextAvailableBlock();
        logicalDisk.disk.pack(-1, nextAvailableBlock*64);
        logicalDisk.disk.pack(nextAvailableBlock, index+4); // INIT FIRST BUFFER BLOCK        
    }

    void deleteFileDescriptorAt(int index) {
        // System.out.printf("==> void FileDescriptor.saveToFileDescriptorIndex(int index = %d);\n", index);        
        logicalDisk.disk.pack(0, index);
        logicalDisk.disk.pack(0, index+4);
        logicalDisk.disk.pack(0, index+8);
        logicalDisk.disk.pack(0, index+12);        
    }
}