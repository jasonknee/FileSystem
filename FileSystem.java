class FileSystem {
    private final int defaultBlockSize = 64;

    public LogicalDisk logicaldisk;
    public Directory directory;
    public FileTable filetable;

    public FileSystem() {
        logicaldisk = new LogicalDisk(defaultBlockSize);
        directory = new Directory(logicaldisk);
        filetable = new FileTable(directory);
    }

    void create(String symbolic_file_name) {}
    void destroy(String symbolic_file_name) {}
    void open(String symbolic_file_name) {}      
    void close(String index) {}
    void read(String index, String mem_area, String count) {}
    void write(String index, String mem_area, String count) {}
    void lseek(String index, String pos) {}    
    void directory() {}      
    void init(String file_name) {}
    void save(String file_name) {}
    
    


    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
    }        
}