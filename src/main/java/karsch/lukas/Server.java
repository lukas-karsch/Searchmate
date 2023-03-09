package karsch.lukas;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Server extends NanoHTTPD {
    private final Path pathToIndex;

    public Server(int port, Path pathToIndex) throws IOException {
        super(port);
        this.pathToIndex = pathToIndex;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.format("Server is running at %d\n", port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String URI = session.getUri();
        System.out.println("Client is requesting " + URI);
        try {
            //TODO: Automatically get the requested file instead of mapping it by hand
            switch(session.getUri()) {
                case "/":
                case "/index.html":
                    return newFixedLengthResponse(Response.Status.OK, "text/html", Files.readString(Path.of("src/main/resources/Webclient/index.html")));
                case "/style.css":
                    return newFixedLengthResponse(Response.Status.OK, "text/css", Files.readString(Path.of("src/main/resources/Webclient/style.css")));
                case "/script.js":
                    return newFixedLengthResponse(Response.Status.OK, "text/javascript", Files.readString(Path.of("src/main/resources/Webclient/script.js")));
                case "/api/search":
                    //TODO: make this prettier
                    HashMap<String, String> body = new HashMap<>();
                    session.parseBody(body);
                    String query = body.get("postData");
                    query = query.substring(query.indexOf(':')+2, query.lastIndexOf("\""));
                    Search s = new Search(pathToIndex);
                    List<SearchResult> results = s.search(query);
                    Gson gson = new Gson();
                    String resultsToJson = gson.toJson(results);
                    return newFixedLengthResponse(Response.Status.OK, "application/json", resultsToJson);
                default:
                    return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/html", Files.readString(Path.of("src/main/resources/Webclient/404.html")));
            }
        }
        catch (IOException e) {
            System.err.println("Something went wrong when providing the file.");
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/html", "<h1>something went wrong on the server</h1>");
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
}
