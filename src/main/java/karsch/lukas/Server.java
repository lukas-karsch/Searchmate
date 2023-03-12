package karsch.lukas;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Server extends NanoHTTPD {
    private final Search search;
    Gson gson;

    public Server(int port, Path pathToIndex) throws IOException {
        super(port);
        this.search = new Search(new Model(pathToIndex));
        gson = new Gson();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.format("Server is running at %d\n", port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String resultsToJson = "";
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
                    resultsToJson = gson.toJson(handleSearch(session));
                    return newFixedLengthResponse(Response.Status.OK, "application/json", resultsToJson);
                case "/api/results":
                    int requestedIndex = Integer.parseInt(session.getQueryParameterString());
                    Path requestedFilePath = Path.of(search.getLastSearchResult().get(requestedIndex).path);
                    return newFixedLengthResponse(Response.Status.OK, "text/html", Files.readString(requestedFilePath));
                default:
                    return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/html", Files.readString(Path.of("src/main/resources/Webclient/404.html")));
            }
        }
        catch (IOException e) {
            System.err.println("Something went wrong when providing the file.");
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html", "<h1>something went wrong on the server</h1>");
        }
        catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/html", "<h1>Bad request</h1><p>You are trying to view a search result that does not exist</p>");
        }
        catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SearchResult> handleSearch(IHTTPSession session) throws ResponseException, IOException {
        String query = getPostData(session);
        query = query.substring(query.indexOf(':')+2, query.lastIndexOf("\""));
        search.search(query);
        return search.getLastSearchResult();
    }

    private String getPostData(IHTTPSession session) throws IOException, ResponseException {
        HashMap<String, String> requestBody = new HashMap<>();
        session.parseBody(requestBody);
        return requestBody.get("postData");
    }
}
