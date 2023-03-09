package karsch.lukas;

public class SearchResult {
    public final String path;
    public final String docName;
    public double weight;

    public SearchResult(String path, String docName, double weight) {
        this.path = path;
        this.weight = weight;
        this.docName = docName;
    }
}
