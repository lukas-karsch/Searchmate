package karsch.lukas;

import java.nio.file.Path;

public class App {
    public void runIndexing() {
        /*
        * Pseudocode
        *
        * for file in directory
        *   tokenize file FileIndex -> <token, freq>
        *   add to file index: DocIndex -> <token, documentCount>
        *   save file to json
        *
        * */
        Path path = Path.of("D:\\Daten\\Programmieren\\Java\\Searchmate\\testdata");
        Model model = new Model();
        model.buildModel(path);
    }

    public void runServer() {
        //TODO: Implement me
    }
}
