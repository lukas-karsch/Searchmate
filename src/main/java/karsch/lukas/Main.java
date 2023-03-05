package karsch.lukas;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        switch (args[0]) {
            case "index" -> {
                System.out.format("Starting indexing of all files in %s\n", args[1]);
                app.runIndexing(args[1], args[2]);
            }
            case "serve" -> {
                System.out.println("Running server at " + args[1]);
            }
        }
    }
}