package karsch.lukas;

import java.nio.file.Path;
import java.util.HashMap;

public class Model {
    //TODO: These fields below need to be serialized and deserialized from / to JSON format
    //Fields should be static (Model is singleton?) and public ?
    private HashMap<String, Frequency> pathToDocIndex;
    private Frequency termDocFreq;

    public Model() {
        pathToDocIndex = new HashMap<>();
        termDocFreq = new Frequency();
    }

    //TODO: implement methods below
    public void buildModel() {}

    public Model deserialize(Path filePath){ return null; }

    public void serialize(){}

}

class Frequency {
    private HashMap<String, Integer> frequency;
}
