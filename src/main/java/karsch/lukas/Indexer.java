package karsch.lukas;

import org.jsoup.Jsoup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

        List<String> allTokens = getTokensFromString(content);
        allTokens.forEach(this::putToken);
        document.numberOfTokens = allTokens.size();
        //TODO: calculate and save TF somewhere here
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
    }

    public List<String> getTokensFromString(String input) {
        if(input.length() == 0) return new ArrayList<>();
        int pos = 0;
        List<String> output = new ArrayList<>();

        while (pos < input.length()) {
            StringBuilder token = new StringBuilder();
            char first = input.charAt(pos);

            if(Character.isAlphabetic(first)) {
                while(pos < input.length() && Character.isAlphabetic(input.charAt(pos))) {
                    token.append(input.charAt(pos));
                    pos++;
                }
                output.add(token.toString().toLowerCase());
            }
            else if(Character.isDigit(first)) {
                while(pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    token.append(input.charAt(pos));
                    pos++;
                }
                output.add(token.toString().toLowerCase());
            }
            else pos++;
        }

        return output;
    }
}
