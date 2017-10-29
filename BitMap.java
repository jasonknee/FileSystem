public class BitMap {
    public LogicalDisk logicaldisk;
    public byte[] bitmap;

    public BitMap(LogicalDisk disk) {
        logicaldisk = disk;
        bitmap = new byte[64];
    }

    public void freeBlockIndex(int blockIndex) {}
}