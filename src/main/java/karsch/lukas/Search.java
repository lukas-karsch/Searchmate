package karsch.lukas;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//TODO: offer several algorithms to the user
//TODO: wenn file ALLE terms aus dem enthält (vielleicht als literal) boost zu weigth geben?

/**
 * This class searches through the indexed files upon being provided with a search query. The serach is done using TF-IDF
 * (Read more about this algorithm here <a href="https://en.wikipedia.org/wiki/Tf">here</a>).
 * This class includes several algorithms to calculate the term weight.
 */
public class Search {
    private final Model model;

    public Search(Model model) {
        this.model = model;
    }

    /**
     * @param query Search query
     * @return Returns a list of Entries< Path, Weight > sorted by their weight.
     */
    public List<SearchResult> search(String query) {
        System.out.format("Searching for '%s'\n", query);
        List<String> tokenized = tokenizeQuery(query.toLowerCase());
        List<SearchResult> results = new ArrayList<>();
        model.pathToDocumentIndex.forEach(
                (path, doc) -> {
                    int N = doc.numberOfTokens;
                    SearchResult result = new SearchResult(path, Path.of(path).getFileName().toString(), 0);
                    tokenized.forEach( token -> {
                        if(doc.counts.containsKey(token)) {
                            double tf = (double) doc.counts.get(token) / N;
                            double idf = -1 * Math.log((double) model.pathToDocumentIndex.size() / model.totalTokenCount.values().stream().reduce(0, Integer::sum));
                            result.weight += tf * idf;
                        }
                    });
                    results.add(result);
        });
        return results.stream()
                .filter((result) -> result.weight != 0)
                .sorted((result1, result2) -> Double.compare(result2.weight, result1.weight))
                .toList();
    }

    //TODO: clean this up / refactor, together with Indexer.java
    private List<String> tokenizeQuery(String query) {
        List<String> tokenized = new ArrayList<>();

        while (query.length() > 0) {
            char first = query.charAt(0);
            if(Character.isWhitespace(first)) {
                query = query.substring(1);
            }
            else if (Character.isDigit(first)) {
                StringBuilder token = new StringBuilder();
                int cut = 0;

                while (cut < query.length() && Character.isDigit(query.charAt(cut))) {
                    token.append(query.charAt(cut));
                    cut++;
                }
                query = query.substring(cut);
                tokenized.add(token.toString());
            }
            else if (Character.isAlphabetic(first)) {
                StringBuilder token = new StringBuilder();
                int cut = 0;

                while (cut < query.length() && (Character.isAlphabetic(query.charAt(cut)) || Character.isDigit(query.charAt(cut)))) {
                    token.append(query.charAt(cut));
                    cut++;
                }
                query = query.substring(cut);
                tokenized.add(token.toString());
            }
            else {
                tokenized.add(String.valueOf(query.charAt(0)));
                query = query.substring(1);
            }
        }

        return tokenized;
    }
}
