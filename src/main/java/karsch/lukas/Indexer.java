package karsch.lukas;

import org.jsoup.Jsoup;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class indexes single files.
 */
public class Indexer {
    private Document document;

    //TODO: include stemming
    public Document indexFile(Path p) {
        document = new Document();
        String content = "";

        try {
            Filetype filetype = Filetype.getFiletype(p.getFileName());
            content = Files.readString(p);
            if (filetype == Filetype.XML || filetype == Filetype.HTML) {
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

        //TODO vielleicht einfach einen index / pointer benutzen anstatt den String ständig neu zu machen
        //TODO: tokenizing should work differently.
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
        return document;
    }
    private void putToken(String token) {
        token = token.toLowerCase();
        if(document.counts.containsKey(token)) {
            document.counts.put(token, document.counts.get(token) + 1);
        }
        else {
            document.counts.put(token, 1);
        }
        document.numberOfTokens++;
    }
}
