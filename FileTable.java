public class FileTable {
    FileTableEntry[] fileTable;
    LogicalDisk logicalDisk;

    public FileTable(LogicalDisk disk) {
        fileTable = new FileTableEntry[3];
        logicalDisk = disk;
    }

    public void alloc() {
        for (int i = 0; i < fileTable.length; i++) {
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
        for (int i = 0; i < fileTable.length; i++) {
            if (fileTable[i].fileLength == -1) {
                return fileTable[i].fileTableIndex;
            }
        }
        return -1;
    }


    public boolean isFree() {
        for (int i = 0; i < fileTable.length; i++) {
            if (fileTable[i].isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public boolean isFileOpen(String filename) {
        for (int i=0; i<fileTable.length; i++) {
            if (filename.equals(fileTable[i].getFileName())) {
                return true;
            }
        }
        return false;
    }

    public int getAvailableFileTableIndex() {
        for (int i=0; i<fileTable.length; i++) {
            if (fileTable[i].isAvailable()) {
                return i;
            }
        }
        return -1;
    }

    public int getLengthOfFile(int index) {
        return logicalDisk.disk.unpack(index);
    }

    public int getBlockIndexOfFile(int index, int fdBlockIndex) {
        System.out.printf("==> void FileTable.getBlockIndexOfFile(int index = %d, int fdBlockIndex = $d);\n", index, fdBlockIndex);        
        return logicalDisk.disk.unpack(index+4 * (4*fdBlockIndex));
    }

    public int allocateEntry(byte[] data, String filename, int length, int position, int index) {
        System.out.printf("==> void FileTable.allocateEntry(int index = %d , int length = %d, int position = %d);\n", index, length, position);        
        int fileTableIndex = getAvailableFileTableIndex();
        if (fileTableIndex != -1) {
            fileTable[fileTableIndex].init(data, filename, length, position, index, fileTableIndex);
            return fileTableIndex;
        }
        return -1;
    }

    public FileTableEntry getFileTableEntry(int index) {
        return fileTable[index];
    }

    public FileTableEntry writeToDisk(int index) {
        return new FileTableEntry(null, "", -1, -1, -1, -1);
    }

    public int findFileDescriptorIndexOfFile(int index) {
        return 0;
    }

    public void freeEntry(int index) {
    }

}