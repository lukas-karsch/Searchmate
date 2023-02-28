package karsch.lukas;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class App {
    public void runIndexing() {
        Path path = Path.of("D:\\Daten\\Programmieren\\Java\\Searchmate\\testdata");
        traverse(path);
    }

    public void traverse(Path p) {
        File dir = new File(p.toUri());
        if(dir.listFiles() == null) return;
        for(File f : dir.listFiles()) {
            if(f.isDirectory()) traverse(f.toPath());
            else {
                System.out.println("Indexing: " + f);
                indexFile(f.toPath())
                        .forEach((key, value) -> System.out.format("   %s => %d\n", key, value));
            }
        }
    }

    public HashMap<String, Integer> indexFile(Path p) {
        HashMap<String, Integer> fileIndex = new HashMap<>();
        fileIndex.put("Hallo", 2);
        fileIndex.put("Joa", 6);
        return fileIndex; //TODO: IMPLEMENT ME
    }

    public void runServer() {
        //TODO: Implement me
    }
}
