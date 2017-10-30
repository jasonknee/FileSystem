import java.util.*;

public class LogicalDisk {
    private static int BLOCK_SIZE = 64;
    public final PackableMemory disk;

    public LogicalDisk(int totalBlocks) {
        disk = new PackableMemory(totalBlocks * BLOCK_SIZE);
    }

    byte[] readBlock(int blockNum) {
        byte[] block = new byte[BLOCK_SIZE];
        for (int j = 0; j < block.length; j++) {
            block[j] = getDisk()[blockNum * BLOCK_SIZE + j];
        }
        return block;
    }

    void writeBlock(int blockIndex, byte[] block) {
        if (block.length != BLOCK_SIZE) {
            System.exit(0);
        }

        for (int j = 0; j < block.length; j++) {
            getDisk()[blockIndex * BLOCK_SIZE + j] = block[j];
        }
    }

    int getIntOfBlockWithIndex(int blockNum, int index) {
        return disk.unpack(blockNum*BLOCK_SIZE + index) ;
    }

    void init(byte[] block) {
        disk.mem = block;
    }

    byte[] getDisk() {
        return disk.mem;
    }

    int getNextAvailableBlock() {
        for (int i=8; i<61; i++) {
            byte[] block = readBlock(i);
            if (Arrays.equals(block, new byte[64])) {
                return i;
            }
        }
        return -1;
    }

    byte[] fullDiskAsByteArray() {
        return disk.mem;
    }

    void printDisk() {
        System.out.printf("=> void printDisk()\n");
        for (int i=0; i<getDisk().length; i=i+16) {
            System.out.printf("%d %d %d %d %d %d %d %d  %d %d %d %d %d %d %d %d\n", 
                                getDisk()[i],
                                getDisk()[i+1],
                                getDisk()[i+2],
                                getDisk()[i+3],
                                getDisk()[i+4],
                                getDisk()[i+5],
                                getDisk()[i+6],
                                getDisk()[i+7],
                                getDisk()[i+8],
                                getDisk()[i+9],
                                getDisk()[i+10],
                                getDisk()[i+11],
                                getDisk()[i+12],
                                getDisk()[i+13],
                                getDisk()[i+14],
                                getDisk()[i+15]);                   
        }
    }

    public static void main(String[] args) {
        LogicalDisk ls = new LogicalDisk(64);
        ls.testLogicalDisk();
    }

    /***************************************************************/
    /*                      TEST LOGICALDISK                       */
    /***************************************************************/

    void testLogicalDisk() {
        byte[] testblock = createBlock();
        this.writeBlock(7, testblock);
        for (int j = 0; j < BLOCK_SIZE; j++) {
            if (testblock[j] == this.readBlock(7)[j]) {
                System.out.printf("read/write test failed: testblock[j] = %d, j = %d\n", testblock[j], j);
                System.exit(0);
            }
        }
        System.out.println("testLogicalDisk() Successful");
    }

    static byte[] createBlock() {
        byte[] block = new byte[BLOCK_SIZE];
        for (byte i = 0; i < BLOCK_SIZE; i++) {
            block[i] = i;
        }
        return block;
    }

    /***************************************************************/
    /*                   / END TEST LOGICALDISK                    */
    /***************************************************************/

}