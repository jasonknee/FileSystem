public class BitMap {
    public LogicalDisk logicaldisk;
    public byte[] bitmap;

    public BitMap(LogicalDisk disk) {
        logicaldisk = disk;
        bitmap = disk.size;
    }
}