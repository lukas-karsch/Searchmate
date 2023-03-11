package karsch.lukas;

import java.util.HashMap;

public class Document {
    public int numberOfTokens;
    public final HashMap<String, Integer> counts;

    public Document() {
        counts = new HashMap<>();
        numberOfTokens = 0;
    }
}
