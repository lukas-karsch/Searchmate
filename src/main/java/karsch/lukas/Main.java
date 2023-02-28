package karsch.lukas;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        switch (args[0]) {
            case "index" -> {
                System.out.println("Starting indexing of all files");
                app.runIndexing();
            }
            case "serve" -> {
                System.out.println("Running server at " + args[1]);
            }
        }
    }
}