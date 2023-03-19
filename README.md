# Searchmate
Local search engine that indexes your files. Input your search query through a simple web interface!
Supported filetypes: 
- plain text
- xml / html / xhtml
- (coming soon) pdf

Read about the TF-IDF algorithm: https://en.wikipedia.org/wiki/Tf–idf

## Run Searchmate
Arguments in square brackets [ ] are optional. Remember to wrap your filepaths in parentheses if they contain whitespaces.

### Index files:
Index all the files inside a directory.
The resulting file is used by the application to search upon being provided with a user query.

<code> < index > < indexing target > < target file > </code>

### Run server:
The server will run at localhost:port (default port is 8080). Access the webclient through any browser and enter your search query.

<code>< serve > [port] < index > </code>

### Count files
Count all files (excluding directories) inside a folder.

<code>count < target folder ></code>
