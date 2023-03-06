package karsch.lukas;

import org.jsoup.Jsoup;

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
            Filetype filetype = Filetype.getFiletype(p.getFileName());
            content = Files.readString(p);
            if (filetype == Filetype.XML) {
                content = Jsoup.parse(content).body().text();
            }
            System.out.format("[INFO] Indexing %s. File contains %d readable characters\n", p.getFileName(), content.length());
        }
        catch (OutOfMemoryError outOfMemErr) {
            System.err.println("[ERR] Could not index file " + p.getFileName() + ". It might be too large.");
        }
        catch (Exception e) {
            System.err.println("An error occured while reading " + p.getFileName());
        }

        //NOTE: vielleicht einfach einen index / pointer benutzen anstatt den String ständig neu zu machen
        while (content.length() > 0) {
            char first = content.charAt(0);
            if(Character.isWhitespace(first)) {
                content = content.substring(1);
            }
            else if (Character.isDigit(first)) {
                StringBuilder token = new StringBuilder();
                int cut = 0;

                while (cut < content.length() && Character.isDigit(content.charAt(cut))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                putToken(token.toString());
            }
            else if (Character.isAlphabetic(first)) {
                StringBuilder token = new StringBuilder();
                int cut = 0;

                while (cut < content.length() && (Character.isAlphabetic(content.charAt(cut)) || Character.isDigit(content.charAt(cut)))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                putToken(token.toString());
            }
            else {
                putToken(String.valueOf(content.charAt(0)));
                content = content.substring(1);
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
