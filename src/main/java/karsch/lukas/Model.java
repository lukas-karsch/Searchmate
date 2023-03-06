package karsch.lukas;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Model {
    //TODO: save amount of tokens "n" per document
    //TODO: just save TF and IDF per term?
    public HashMap<String, HashMap<String, Integer>> pathToDocumentIndex;   //maps Paths to their indexed document
    public HashMap<String, Integer> totalTokenCount;                        //maps every single token to their count across all documents

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
            }
        }
    }

    private void addToWortCount(HashMap<String, Integer> docIndex) { //TODO: this should only increment the counter by 1 per document?? maybe??
        docIndex.forEach((token, count) -> {
            totalTokenCount.
                    put(token, totalTokenCount.getOrDefault(token, 0) + count);
        });
    }

    /**
     * Reads a JSON file containing the results of indexing into Model
     * @param filePath Path to the json file that stores the results of file indexation
     * @return Model object
     */
    public Model deserialize(Path filePath){
        final Gson DESERIALIZER = new Gson();
        Model deserialized;
        try {
            deserialized = DESERIALIZER.fromJson(filePath.toString(), Model.class);
        }
        catch (JsonSyntaxException e) {
            System.err.format("[ERROR] Could not read index file %s\n", filePath);
            e.printStackTrace();
            return null;
        }
        System.out.format("Loaded %s successfully!\n", filePath.getFileName());
        return deserialized;
    }

    /**
     * Serializes the Model into JSON for later use
     * @param saveTo Path that the JSON will be saved to
     */
    public void serialize(Path saveTo){
        final Gson SERIALIZER = new Gson();
        final String JSON = SERIALIZER.toJson(this);
        try {
            Files.createDirectories(saveTo);
            Files.writeString(Path.of(saveTo + "\\index.json"), JSON);
        }
        catch(IOException io) {
            System.err.format("[ERROR] Could not write the indexing results to file in Path %s\n", saveTo);
            io.printStackTrace();
            return;
        }
        System.out.format("Serialized and saved to %s\n", saveTo);
    }

    public int getDocumentAmount(Path p, int current) {
        File dir = new File(p.toUri());
        File[] allFiles = dir.listFiles();
        if(allFiles == null) return current;

        for(File f : allFiles) {
            if(f.isDirectory()) current = getDocumentAmount(f.toPath(), current);
            else {
                current++;
            }
        }
        return current;
    }
}
