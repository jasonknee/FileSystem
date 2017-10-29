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

    byte[] getDisk() {
        return disk.mem;
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