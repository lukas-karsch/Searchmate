package karsch.lukas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * This class indexes single files.
 */
public class Indexer {
    public HashMap<String, Integer> indexFile(Path p) {
        HashMap<String, Integer> fileIndex = new HashMap<>();
        String content = "";

        try {
            content = Files.readString(p);
            System.out.println("[INFO] Indexing " + p.getFileName());
        }
        catch (IOException ioErr) {
            ioErr.printStackTrace();
        }
        catch (OutOfMemoryError outOfMemoryError) {
            System.err.println("Could not index file " + p.getFileName() + " because it is too large.");
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
                String finalToken = token.toString();
                if(fileIndex.containsKey(finalToken)) {
                    fileIndex.put(finalToken, fileIndex.get(finalToken) + 1);
                }
                else {
                    fileIndex.put(finalToken, 1);
                }
            }
            else if (Character.isAlphabetic(first)) {
                StringBuilder token = new StringBuilder(first);
                int cut = 0;

                while (Character.isAlphabetic(content.charAt(cut)) || Character.isDigit(content.charAt(cut))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                String finalToken = token.toString();
                if(fileIndex.containsKey(finalToken)) {
                    fileIndex.put(finalToken, fileIndex.get(finalToken) + 1);
                }
                else {
                    fileIndex.put(finalToken, 1);
                }
            }
            else {
                StringBuilder token = new StringBuilder(first);
                int cut = 0;

                while (!Character.isAlphabetic(content.charAt(cut)) && !Character.isDigit(content.charAt(cut)) && !Character.isWhitespace(content.charAt(cut))) {
                    token.append(content.charAt(cut));
                    cut++;
                }
                content = content.substring(cut);
                String finalToken = token.toString();
                if(fileIndex.containsKey(finalToken)) {
                    fileIndex.put(finalToken, fileIndex.get(finalToken) + 1);
                }
                else {
                    fileIndex.put(finalToken, 1);
                }
            }
        }
        return fileIndex;
    }
}
