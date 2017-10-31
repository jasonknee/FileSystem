import java.util.*;

public class FileTableEntry {
    public byte[] bufferData;
    public String fileName;
    public int fileLength;
    public int positionInFile;
    public int fileDescriptorIndex;
    public int fileTableIndex;
    public boolean available;
    LogicalDisk logicalDisk;
    Directory directory;

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

    public FileTableEntry(LogicalDisk disk, Directory dir, int index) {
        bufferData = null;
        fileName = "";
        fileLength = -1;
        positionInFile = 0;
        fileDescriptorIndex = -1;
        fileTableIndex = index;
        available = true;
        logicalDisk = disk;
        directory = dir;
    }

    public void dealloc(int index) {
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

    public int getFileLength() {
        return fileLength;
    }

    public int getPosition() {
        return positionInFile;
    }

    public byte[] getBufferBlock() {
        return bufferData;
    }

    public int getBufferBlockNum() {
        if (0 <= positionInFile && positionInFile < 64) {
            return 0;
        }
        if (64 <= positionInFile && positionInFile < 128) {
            return 1;
        }
        if (128 <= positionInFile && positionInFile < 192) {
            return 2;
        }
        else {
            return -1;
        }
    }

    public int printBuffer() {
        System.out.printf("=> FileTableEntry.printBuffer()\n");        
        int dist = 0;
        for (int p = positionInFile%64; p < 64; p++,dist++) {
            byte[] feed = Arrays.copyOfRange(bufferData, p, 63);
            String printMe = new String(feed);
            System.out.printf("%s", printMe);
        }
        moveToPosition(positionInFile + dist);        
        return dist;
    }

    public int getNextBlockIndex() {
        int x = directory.getBlockIndexOfFile(fileDescriptorIndex, getBufferBlockNum()+1);
        if (0 < x && x < 4) {
            return x;
        }
        return -1;        
    }

    public void loadBlock(byte[] block) {
        deallocBlock();     
        bufferData = block;
    }

    public void deallocBlock() {
        byte[] empty = new byte[64];
        bufferData = null;
        bufferData = empty;   
    }

    public void moveToPosition(int x) {
        positionInFile = x;
    }

    public byte getCurrentByte() {
        return bufferData[positionInFile%64];
    }

    public boolean canRead() {
        return positionInFile < fileLength;
    }

    public boolean canWrite() {
        System.out.printf("=> FileTableEntry.canWrite() = %s\n", positionInFile<192 ? "true" : "false");                
        return positionInFile < 192;
    }

    public void write(char c) {
        System.out.printf("=> FileTableEntry.write(char c = %c)\n", c);
        fileLength++;
        bufferData[(positionInFile)%64] = (byte) c;
    }

    public boolean endOfBufferBlock() {
        // System.out.printf("=> FileTableEntry.endOfBufferBlock() = %s, position: %d\n", positionInFile+1%64 == 0 ? "true" : "false", positionInFile);                                        
        return (positionInFile)%64 == 0 && positionInFile != 0;
    }

    public void writeCurrentAndLoadNextBlock() {
        System.out.printf("=> FileTableEntry.writeCurrentAndLoadNextBlock()\n");
        int bufferBlockNum = getBufferBlockNum();
        if (bufferBlockNum > 2) {
            return;
        }

        writeBlock(bufferBlockNum-1);
        deallocBlock();
        loadBlock(bufferBlockNum);                                        
    }

    public void writeCurrentAndLoadSpecifiedBlock(int blockNum) {
        System.out.printf("=> FileTableEntry.writeCurrentAndLoadNextBlock()\n");
        int bufferBlockNum = getBufferBlockNum();
        if (bufferBlockNum > 2 || blockNum < 0 || blockNum > 2) {
            return;
        }
        
        writeBlock(bufferBlockNum);
        deallocBlock();        
        loadBlock(blockNum);
    }

    public int incrementPosition() {
        // System.out.printf("=> FileTableEntry.incrementPosition()\n");                                
        // if (positionInFile == fileLength-1) {
        //     return -1;
        // }
        return positionInFile++;
    }

    public void writeBlock(int blockNum) {
        System.out.printf("=> FileTableEntry.writeBlock(int blockNum = %d)\n", blockNum);
        int blockPointer = logicalDisk.disk.unpack(fileDescriptorIndex+4 + (4*blockNum));
        System.out.printf("THE BLOCK POINTER = %d", blockPointer);
        logicalDisk.disk.pack(fileLength,fileDescriptorIndex);
        logicalDisk.writeBlock(blockPointer, bufferData);
    }

    public void loadBlock(int blockNum) {
        System.out.printf("=> FileTableEntry.loadNextBlock(int blockNum = %d)\n", blockNum);       
        // int blockPointer = logicalDisk.disk.unpack(fileDescriptorIndex+4 + (4*blockNum));
        // byte[] nextBlock = logicalDisk.readBlock(blockPointer);
        // loadBlock(nextBlock);
        if (0 <= blockNum && blockNum < 3) {            
            int x = directory.getBlockIndexOfFile(fileDescriptorIndex, blockNum);
            byte[] nextBlock = logicalDisk.readBlock(x);
            loadBlock(nextBlock);
        }
    }

    public boolean canMoveTo(int newPos) {
        return 0 <= newPos && newPos < fileLength;
    }

    public void moveTo(int newPos) {
        int newBlockNum;

        if (0 < newPos && newPos < 64) {
            newBlockNum = 0;
        }
        else if (64 < newPos && newPos < 128) {
            newBlockNum = 1;            
        }
        else if (128 < newPos && newPos < 192) {
            newBlockNum = 2;            
        } else {
            return;
        }

        if (newBlockNum != getBufferBlockNum()) {
            writeCurrentAndLoadSpecifiedBlock(newBlockNum);
        }
        positionInFile = newPos;
    }

    public byte[] readFile(int count) { return new byte[64]; };   
    public int writeFile(char c, int count) { return 0; }
}