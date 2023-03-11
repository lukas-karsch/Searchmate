package karsch.lukas;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Model {
    //TODO: save TF (and IDF?)
    public HashMap<String, Document> pathToDocumentIndex;   //maps Paths to their indexed document
    public HashMap<String, Integer> totalTokenCount;                        //maps every single token to their count across all documents

    public Model() {
        pathToDocumentIndex = new HashMap<>();
        totalTokenCount = new HashMap<>();
    }

    /**
     * This constructor creates a Model by deserializing an index file. If this file is invalid, an empty Model will be created
     * @param p Path to the file containing indexing results
     */
    public Model(Path p) {
        Model deserialized = deserializeFromJson(p);
        if(deserialized == null) {
            System.err.println("[ERROR] The provided file seems to not exist or contain invalid information.");
            pathToDocumentIndex = new HashMap<>();
            totalTokenCount = new HashMap<>();
        }
        else {
            pathToDocumentIndex = deserialized.pathToDocumentIndex;
            totalTokenCount = deserialized.totalTokenCount;
        }
    }

    /**
     * Looping through all files inside a directory. Files are then tokenized, indexed and added to the model.
     * @param path Path to the directory that shall be searched
     */
    public void buildModel(Path path) {
        File dir = new File(path.toUri());
        File[] allFiles = dir.listFiles();
        if(allFiles == null) return;

        Indexer indexer = new Indexer();
        for(File f : allFiles) {
            if(f.isDirectory()) buildModel(f.toPath());
            else {
                Document doc = indexer.indexFile(f.toPath());
                pathToDocumentIndex.put(f.getPath(), doc);
                addToWortCount(doc);
            }
        }
    }

    /**
     * This method is called after indexing a document. All the tokens in that document are added to the model's total token count.
     * @param docIndex Indexed document
     */
    private void addToWortCount(Document document) {
        document.counts.forEach((token, count) ->
            totalTokenCount.put(token, totalTokenCount.getOrDefault(token, 0) + count)
        );
    }

    /**
     * Reads a JSON file containing the results of indexing into Model
     * @param filePath Path to the json file that stores the results of file indexation
     * @return Model object from file. Returns Null if the provided file is invalid
     */
    public static Model deserializeFromJson(Path filePath) {
        final Gson DESERIALIZER = new Gson();
        Model deserialized;
        try {
            deserialized = DESERIALIZER.fromJson(Files.readString(filePath), Model.class);
        }
        catch (JsonSyntaxException e) {
            System.err.format("[ERROR] Could not read index file %s\n", filePath);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.format("Loaded %s successfully!\n", filePath.getFileName());
        return deserialized;
    }

    /**
     * Serializes the Model into JSON for later use
     * @param saveTo Path that the JSON will be saved to
     */
    public void serializeToJson(Path saveTo) {
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

    /**
     * Loops through the specified directory to get the number of files inside.
     * @param p Path that shall be scanned
     * @param current For the recursive call
     * @return How many files are inside the specified Path p (excluding folders)
     */
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
