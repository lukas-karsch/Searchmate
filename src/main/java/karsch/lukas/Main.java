package karsch.lukas;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        try {
            switch (args[0]) {
                case "index" -> {
                    System.out.format("Starting indexing of all files in %s\n", args[1]);
                    app.runIndexing(args[1], args[2]);
                }
                case "serve" -> {
                    System.out.println("Listening at " + args[1]);
                }
                case "search" -> {
                    System.out.println("Counting all files...");
                    app.countAllFiles(args[1]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Canceling.");
            System.err.println("An error occured. Check the README.md file for infos on usage.");
        }
    }
}