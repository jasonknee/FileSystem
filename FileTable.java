public class FileTable {
    FileTableEntry[] fileTable;
    LogicalDisk logicalDisk;
    Directory directory;

    public FileTable(LogicalDisk disk, Directory dir) {
        fileTable = new FileTableEntry[3];
        logicalDisk = disk;
        directory = dir;
    }

    public void alloc() {
        for (int i = 0; i < fileTable.length; i++) {
            fileTable[i] = new FileTableEntry(logicalDisk, directory, i);
        }
    }

    public int insert(String filename, int index) {
        // System.out.printf("==> void FileTable.insert(String filename = %s, int index = %d);\n", filename, index);

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
        // System.out.printf("==> void FileTable.getBlockIndexOfFile(int index = %d, int fdBlockIndex = %d);\n", index, fdBlockIndex);        
        return logicalDisk.disk.unpack(index + 4 + (4*fdBlockIndex));
    }

    public int allocateEntry(byte[] data, String filename, int length, int position, int index) {
        // System.out.printf("==> void FileTable.allocateEntry(int index = %d , int length = %d, int position = %d);\n", index, length, position);        
        int fileTableIndex = getAvailableFileTableIndex();
        if (fileTableIndex == -1) {
            return -1;
        }
                    
        fileTable[fileTableIndex].init(data, filename, length, position, index, fileTableIndex);
        return fileTableIndex;
    }

    public FileTableEntry getFileTableEntry(int index) {
        return fileTable[index];
    }

    public void writeToDisk(int index) {
        FileTableEntry fileToDelete = getFileTableEntry(index);
        int fdBlockIndex = fileTable[index].getBufferBlockNum();
        fileToDelete.writeBlock(fdBlockIndex);
    }

    public int readFile(int fileTableIndex, int count) {
        // System.out.printf("==> void FileTable.readFile(int index = %d, int count = %d);\n", fileTableIndex, count);                
        FileTableEntry fileToRead = getFileTableEntry(fileTableIndex);
        int bytesRead = 0;
        char[] feed = new char[count];
        while(fileToRead.canRead() && bytesRead < count) {
            if (fileToRead.endOfBufferBlock()) {
                fileToRead.writeCurrentAndLoadNextBlock();
            }
            
            feed[bytesRead] = (char)fileToRead.getCurrentByte();
            System.out.printf("%c",feed[bytesRead]);            
            bytesRead++;            
            if(-1 == fileToRead.incrementPosition()) {
                break;
            }
        }
        System.out.printf("\n");
        return bytesRead;
    }

    public int  writeCharsToFile(int fileTableIndex, char c, int count) {
        // System.out.printf("==> void FileTable.writeCharsToFile(int fileTableIndex = %d, char c = %c, int count = %d);\n", fileTableIndex, c, count);                
        FileTableEntry fileToWrite = getFileTableEntry(fileTableIndex);        
        int bytesWritten = 0;
        while(fileToWrite.canWrite() && bytesWritten < count) {
            if (fileToWrite.endOfBufferBlock()) {
                fileToWrite.writeCurrentAndLoadNextBlock();
            }

            fileToWrite.write(c);
            fileToWrite.incrementPosition();
            bytesWritten++;
        }
      
        // fileToWrite.writeBlock(fileToWrite.getBufferBlockNum());  
        return bytesWritten;      
    }
        // while (fileToRead.getPosition() < fileToRead.getFileLength()) {
        //     bytesRead = bytesRead + fileToRead.printBuffer();
        //     writeToDisk(index);

        //     int nextBlockIndex = fileToRead.getNextBlockIndex(directory);
        //     if (nextBlockIndex != -1) {
        //         return 0;
        //     }

        //     byte[] nextBlock = logicalDisk.readBlock(nextBlockIndex);
        //     fileToRead.loadBlock(nextBlock);
        // }
        // return bytesRead;

    public boolean moveToPosition(int fileTableIndex, int position) {
        FileTableEntry file = getFileTableEntry(fileTableIndex);    
        if (file.canMoveTo(position)) {
            file.moveTo(position);
            return true;
        }    
        return false;
    }
    public int findFileDescriptorIndexOfFile(int index) {
        return 0;
    }

    public void freeEntry(int index) {
    }

}