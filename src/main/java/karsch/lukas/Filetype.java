package karsch.lukas;

import java.nio.file.Path;

/**
 * Matches a file extension to its filetype.
 * Supported filetypes as of right now are XML and plain text
 */
//TODO: add PDF support
public enum Filetype {
    XML,
    PLAIN;

    public static Filetype getFiletype(Path fileName) {
        String filename = fileName.getFileName().toString();
        String extension = filename.substring(filename.lastIndexOf('.') + 1);

        return switch (extension) {
            case "xml", "html", "xhtml" -> Filetype.XML;
            default -> PLAIN;
        };
    }
}
