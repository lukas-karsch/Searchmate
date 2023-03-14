package karsch.lukas;

import java.nio.file.Path;

/**
 * Matches a file extension to its filetype.
 * Supported filetypes as of right now are XML and plain text
 */
//TODO: add PDF support
public enum Filetype {
    XML,
    HTML,
    PDF,
    PLAIN;

    public static Filetype getFiletype(Path fileName) {
        String filename = fileName.getFileName().toString();
        String extension = filename.substring(filename.lastIndexOf('.') + 1);

        return switch (extension) {
            case "html", "xhtml" -> Filetype.HTML;
            case "xml" -> Filetype.XML;
            case "pdf" -> Filetype.PDF;
            default -> PLAIN;
        };
    }

    public static String getMimeType(Filetype filetype) {
        return switch(filetype) {
            case HTML -> "text/html";
            case PDF -> "application/pdf";
            default -> "text/plain";
        };
    }
}
