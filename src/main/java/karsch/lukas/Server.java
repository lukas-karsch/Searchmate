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
    private final Path BASE_PATH = Path.of("src/main/resources/Webclient/");
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
        Path requestedPath = Path.of(BASE_PATH + URI);
        System.out.println("Client is requesting " + requestedPath);
        try {
            switch (session.getUri()) {
                case "/api/search" -> {
                    resultsToJson = gson.toJson(handleSearch(session));
                    return newFixedLengthResponse(Response.Status.OK, "application/json", resultsToJson);
                }
                case "/api/results" -> { //TODO: This should use query string instead of HTTP post
                    int requestedIndex = Integer.parseInt(session.getQueryParameterString());
                    SearchResult requestedResult = search.getLastSearchResult().get(requestedIndex);
                    Path requestedFilePath = Path.of(requestedResult.path);
                    return newFixedLengthResponse(Response.Status.OK, Filetype.getMimeType(requestedResult.filetype), Files.readString(requestedFilePath));
                }
                default -> {
                    if (Files.isDirectory(requestedPath)) {
                        return newFixedLengthResponse(Response.Status.OK, Files.probeContentType(requestedPath), Files.readString(Path.of(requestedPath + "/index.html")));
                    }
                    return newFixedLengthResponse(Response.Status.OK, Files.probeContentType(requestedPath), Files.readString(requestedPath));
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/html", "<h1>Bad request</h1><p>You are trying to view a search result that does not exist</p>");
        }
        catch (IOException e) {
            System.err.println("Something went wrong when providing the file.");
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html", "<h1>something went wrong on the server</h1>");
        }
        catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SearchResult> handleSearch(IHTTPSession session) throws ResponseException, IOException {
        String query = getPostData(session);
        query = query.substring(query.indexOf(':')+2, query.lastIndexOf("\"")); //TODO: Make SearchRequest a class / object?
        search.search(query);
        return search.getLastSearchResult();
    }

    private String getPostData(IHTTPSession session) throws IOException, ResponseException {
        HashMap<String, String> requestBody = new HashMap<>();
        session.parseBody(requestBody);
        return requestBody.get("postData");
    }
}
