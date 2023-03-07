package karsch.lukas;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class App {
    /**
     * This method indexes the target folder and saves results of the operation to <code>targetFile</code>.
     * @param indexingTarget Path to the folder that the indexing will be run on
     * @param targetFile Path to the folder where the index.json file will be saved to. This file contains all results of indexing and
     *                 needs to be supplied to runServer() when trying to provide queries later on.
     */
    public void runIndexing(String indexingTarget, String targetFile) {
        Path indexingTargetPath, targetFilePath;
        try {
            indexingTargetPath = Path.of(indexingTarget);
            targetFilePath = Path.of(targetFile);
        }
        catch (InvalidPathException invalidPath) {
            System.err.println("[ERROR] Could not parse one of your supplied paths.");
            return;
        }
        Model model = new Model();
        model.buildModel(indexingTargetPath);
        model.serializeToJson(targetFilePath);
    }

    /**
     * Counts all files inside a folder
     * @param path Path to the folder
     */
    public void countAllFiles(String path) {
        Model model = new Model();
        System.out.println(model.getDocumentAmount(Path.of(path), 0));
    }

    public void runServer() {
        //TODO: Implement me
    }

    public void runSearch(String pathToIndexFile) {
        Path p = Path.of(pathToIndexFile); //TODO: add try catch
        Search search = new Search(p);
        System.out.println("Searching in " + p.getFileName());
        search.search("i hate my life")
                .forEach((key, value) -> System.out.format("%6f: %s\n", value, key));
    }
}
