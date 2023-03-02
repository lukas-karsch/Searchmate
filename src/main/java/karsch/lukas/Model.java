package karsch.lukas;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class Model {
    //TODO: These fields below need to be serialized and deserialized from / to JSON format
    //TODO: save amount of tokens in a document
    public HashMap<String, HashMap<String, Integer>> pathToDocumentIndex; //maps Paths to their indexed document
    public HashMap<String, Integer> totalTokenCount; //maps every single token to their count across all documents

    public Model() {
        pathToDocumentIndex = new HashMap<>();
        totalTokenCount = new HashMap<>();
    }

    public void buildModel(Path path) {
        File dir = new File(path.toUri());
        File[] allFiles = dir.listFiles();
        if(allFiles == null) return;

        Indexer indexer = new Indexer();
        for(File f : allFiles) {
            if(f.isDirectory()) buildModel(f.toPath());
            else {
                HashMap<String, Integer> docIndex = indexer.indexFile(f.toPath());
                pathToDocumentIndex.put(f.getPath(), docIndex);
                addToWortCount(docIndex);
                docIndex
                    .forEach((key, value) -> System.out.format("    %s => %d\n", key, value));
            }
        }
    }

    private void addToWortCount(HashMap<String, Integer> docIndex) {
        docIndex.forEach((token, count) -> {
            totalTokenCount.put(
                    token, totalTokenCount.getOrDefault(token, 1)  //TODO: FIX ME This does not work!! always puts 1 as the value
            );
        });
    }

    //TODO: implement methods below

    public Model deserialize(Path filePath){ return null; }

    public void serialize(){}
}