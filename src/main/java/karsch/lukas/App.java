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
    public void runIndexing(String indexingTarget, String targetFile) throws InvalidPathException {
        final Path indexingTargetPath = Path.of(indexingTarget);
        final Path targetFilePath = Path.of(targetFile);

        final Model model = new Model();
        model.buildModel(indexingTargetPath);
        model.serializeToJson(targetFilePath);
    }

    /**
     * Counts all files inside a folder
     * @param path Path to the folder
     */
    public void countAllFiles(String path) throws InvalidPathException {
        Model model = new Model();
        System.out.println(model.getDocumentAmount(Path.of(path), 0));
    }

    public void runServer() {
        //TODO: Implement me
    }

    public void runSearch(String pathToIndexFile) throws InvalidPathException {
        Path p = Path.of(pathToIndexFile); //TODO: add try catch
        Search search = new Search(p);
        System.out.println("Searching in " + p.getFileName());
        search.search("compare double")
                .forEach(e -> System.out.format("%6f: %s\n", e.getValue(), e.getKey()));
    }
}
