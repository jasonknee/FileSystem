public class Directory {
    public LogicalDisk logicaldisk;

    /* Directory entries */
    /* every pair store file descriptor index and file name. */
    // dir_entries[0] = 'abc\n'
    // dir_entries[1] = i;
    private int dir_entries[];

    public Directory(LogicalDisk disk) {
        logicaldisk = disk;
    }
}