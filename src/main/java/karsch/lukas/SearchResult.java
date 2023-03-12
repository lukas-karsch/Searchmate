package karsch.lukas;

public class SearchResult {
    public final String path;
    public final String docName;
    public double weight;
    public Filetype filetype;

    public SearchResult(String path, String docName, double weight, Filetype filetype) {
        this.path = path;
        this.weight = weight;
        this.docName = docName;
        this.filetype = filetype;
    }
}
