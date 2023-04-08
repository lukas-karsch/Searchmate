package karsch.lukas;

public class SearchResult {
    public final String path;
    public final String docName;
    public double weight = 0;
    public Filetype filetype;

    public SearchResult(String path, String docName, Filetype filetype) {
        this.path = path;
        this.docName = docName;
        this.filetype = filetype;
    }
}
