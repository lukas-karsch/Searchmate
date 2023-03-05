package karsch.lukas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * This class indexes single files.
 */
public class Indexer {
    private HashMap<String, Integer> frequency;

    public HashMap<String, Integer> indexFile(Path p) {
        frequency = new HashMap<>();
        String content = "";

        try {
            content = Files.readString(p);
            System.out.println("[INFO] Indexing " + p.getFileName());
        }
        catch (IOException ioErr) {
            ioErr.printStackTrace();
        }
        catch (OutOfMemoryError outOfMemoryError) {
            System.err.println("[ERR] Could not index file " + p.getFileName() + ". It might be too large.");
        }

        while (content.length() > 0) {
            char first = content.charAt(0);
            if(Character.isWhitespace(first)) {
                content = content.substring(1);
            }
            else if (Character.isDigit(first)) {
                StringBuilder token = new StringBuilder(first);
                int cut = 0;

                while (Character.isDigit(content.charAt(cut))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                putToken(token.toString());
            }
            else if (Character.isAlphabetic(first)) {
                StringBuilder token = new StringBuilder(first);
                int cut = 0;

                while (Character.isAlphabetic(content.charAt(cut)) || Character.isDigit(content.charAt(cut))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                putToken(token.toString());
            }
            else { //NOTE: sollte das sonderzeichen aneinanderhängen oder trennen?
                StringBuilder token = new StringBuilder(first);
                int cut = 0;

                while (!Character.isAlphabetic(content.charAt(cut)) && !Character.isDigit(content.charAt(cut)) && !Character.isWhitespace(content.charAt(cut))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                putToken(token.toString());
            }
        }
        return frequency;
    }

    private void putToken(String token) {
        token = token.toUpperCase();
        if(frequency.containsKey(token)) {
            frequency.put(token, frequency.get(token) + 1);
        }
        else {
            frequency.put(token, 1);
        }
    }
}
