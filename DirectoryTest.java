public class DirectoryTest {
    private static int BLOCK_SIZE = 64*64;
    
    private static LogicalDisk mockLogicalDisk;    
    private static Directory mockDirectory;

    public DirectoryTest() {
        mockLogicalDisk = new LogicalDisk(BLOCK_SIZE);
        mockDirectory = new Directory(mockLogicalDisk);

    }

    public static void testArrayOfFileNames() {
        System.out.printf("==> int DirectoryTest.testArrayOfFileNames();\n");
    }

    public static void testFindFileIndex() {
        System.out.printf("==> int DirectoryTest.testFindFileIndex();\n");
        generateTestDirectory();
        int resultIndex = mockDirectory.findFileDescriptorIndexOfFile("abc");
        
        if (resultIndex != expectedIndex) {
            System.out.printf("void testFindFileIndex() => expected: %d, result: %d", expectedIndex, resultIndex);
        }
    }

    private void generateTestDirectory() {
        System.out.printf("==> int Directory.createTestDirectory()");        
        
        byte[] directoryBlock = getDirectoryBlock();
        directoryBlock[1] = 10;    
        directoryBlock[2] = 20;
        directoryBlock[3] = 30;
        logicalDisk.writeBlock(1, directoryBlock);
    }

    private void createTestDirectory() {
        System.out.printf("==> int Directory.createTestDirectory()");        
        
        byte[] directoryBlock = getDirectoryBlock();
        directoryBlock[1] = 10;
        directoryBlock[2] = 20;
        directoryBlock[3] = 30;
        for (int i=1; i<4; i++) { // FOR EACH DIRECTORY BLOCK
            createTestDirectoryEntries(directoryBlock[i]);                      
        }
        logicalDisk.writeBlock(1, directoryBlock);
    }

    private void createTestDirectoryEntries(byte blockNum) {
        String testFileName = "tst\0";
        for (int j=0; j<64; j=j+8) {
            logicalDisk.disk.packString(testFileName, 64*blockNum+j);
            logicalDisk.disk.pack(j, 64*blockNum+j+3);          
        }
    }

    public static void main(String[] args) {
    }        
}