package karsch.lukas;

import java.io.File;
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
        traverse(path);
    }

    //TODO: this method should return the Model
    public void traverse(Path p) {
        Model model = new Model();
        File dir = new File(p.toUri());
        if(dir.listFiles() == null) return;

        Indexer indexer = new Indexer();
        for(File f : dir.listFiles()) {
            if(f.isDirectory()) traverse(f.toPath());
            else {
                indexer.indexFile(f.toPath())
                        .forEach((key, value) -> System.out.format("    %s => %d\n", key, value));
            }
        }
    }

    public void runServer() {
        //TODO: Implement me
    }
}
