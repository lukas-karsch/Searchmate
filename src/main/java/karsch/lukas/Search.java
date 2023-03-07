package karsch.lukas;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class searches through the indexed files upon being provided with a search query. The serach is done using TF-IDF
 * (Read more about this algorithm here <a href="https://en.wikipedia.org/wiki/Tf">here</a>).
 * This class includes several algorithms to calculate the term weight.
 */
public class Search {
    private final Model model;

    public Search(Path pathToIndex) {
        model = new Model(pathToIndex);
    }

    public HashMap<String, Double> search(String query) { //TODO: this should return an array list of the top ___ results
        List<String> tokenized = tokenizeQuery(query.toLowerCase());
        HashMap<String, Double> weights = new HashMap<>(); //Maps each document to its weight (relevancy) to the query
        model.pathToDocumentIndex.forEach(
                (path, freq) -> {
                    int totalTokenAmt = freq.size(); //TODO: This is incorrect
                    tokenized.forEach( token -> {
                                if(freq.containsKey(token)) {
                                    double tf = (double) freq.get(token) / totalTokenAmt;
                                    double idf = -1 * Math.log((double) model.pathToDocumentIndex.size() / model.totalTokenCount.values().stream().reduce(0, Integer::sum)); //TODO: What base should be used here?
                                    double weight = tf * idf;
                                    weights.put(path, weight + weights.getOrDefault(token, 0.0));
                                }
                            }
                    );
                }
        );
        return weights;
    }

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
