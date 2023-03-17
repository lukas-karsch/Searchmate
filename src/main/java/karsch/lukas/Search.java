package karsch.lukas;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//TODO: offer several algorithms to the user
//TODO: wenn file ALLE terms aus dem enthält (vielleicht als literal) boost zu weight geben?

/**
 * This class searches through the indexed files upon being provided with a search query. The serach is done using TF-IDF
 * (Read more about this algorithm here <a href="https://en.wikipedia.org/wiki/Tf">here</a>).
 * This class includes several algorithms to calculate the term weight.
 */
public class Search {
    private final Model model;
    private List<SearchResult> lastSearchResult = new ArrayList<>();
    private final Indexer indexer = new Indexer();

    public Search(Model model) {
        this.model = model;
    }

    /**
     * Searches for tokens of the query inside the model
     * @param query Search query
     */
    public void search(String query) {
        System.out.format("Searching for '%s'\n", query);
        List<String> tokenized = indexer.getTokensFromString(query.toLowerCase());
        List<SearchResult> results = new ArrayList<>();
        model.pathToDocumentIndex.forEach(
                (path, doc) -> {
                    int N = doc.numberOfTokens;
                    Path p = Path.of(path);
                    SearchResult result = new SearchResult(path, p.getFileName().toString(), 0, Filetype.getFiletype(p));
                    tokenized.forEach( token -> {
                        if(doc.counts.containsKey(token)) {
                            double tf = (double) doc.counts.get(token) / N;
                            double idf = -1 * Math.log((double) model.pathToDocumentIndex.size() / model.totalTokenCount.values().stream().reduce(0, Integer::sum));
                            result.weight += tf * idf;
                        }
                    });
                    results.add(result);
        });
        //The more results a search yields, the longer it takes to respond to a request.
        //TODO: test the correlation (linear / quadratic?)
        lastSearchResult = results.stream()
                .filter((result) -> result.weight != 0)
                .sorted((result1, result2) -> Double.compare(result2.weight, result1.weight))
                .toList();
    }

    public  List<SearchResult> getLastSearchResult() {
        return lastSearchResult;
    }

}
