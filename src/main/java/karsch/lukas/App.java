package karsch.lukas;

import java.nio.file.Path;

public class App {
    /**
     * This method indexes the target folder and saves results of the operation to <code>filePath</code>.
     * @param indexingTarget Path to the folder that the indexing will be run on
     * @param filePath Path to the folder where the index.json file will be saved to. This file contains all results of indexing and
     *                 needs to be supplied to runServer() when trying to provide queries later on.
     */
    public void runIndexing(String indexingTarget, String filePath) {
        /*
        * Pseudocode
        *
        * for file in directory
        *   tokenize file FileIndex -> <token, freq>
        *   add to file index: DocIndex -> <token, documentCount>
        *   save file to json
        *
        * */
        Path path = Path.of(indexingTarget);
        Model model = new Model();
        model.buildModel(path);
        model.serialize(Path.of("D:\\temp\\index"));
    }

    public void runServer() {
        //TODO: Implement me
    }
}
