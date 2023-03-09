package karsch.lukas;

import java.nio.file.InvalidPathException;

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
                    app.runServer(Integer.parseInt(args[1]), args[2]);
                }
                case "count" -> {
                    System.out.println("Counting all files...");
                    app.countAllFiles(args[1]);
                }
                case "search" -> {
                    System.out.println("Searching...");
                    app.runSearch(args[1]);
                }
                default ->
                    System.err.println("Unknown command. Check the README.md file for infos on usage.");
            }
        }
        catch (InvalidPathException invalidPath) {
            System.err.print("[ERROR] Looks like you specified an invalid path.");
        }
        catch (NumberFormatException numExc) {
            System.err.println("Could not parse your input into a number.");
        }
        catch (Exception e) {
            System.err.println("Canceling.");
            System.err.println("An error occured. Check the README.md file for infos on usage.");
            e.printStackTrace();
        }
    }
}