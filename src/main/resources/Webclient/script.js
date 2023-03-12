function sendSearchRequest(query) {
    console.log(`Requesting "${query}"`);
    const resultBox = document.body.querySelector("#results > ul");
    resultBox.innerHTML = "";


    fetch("/api/search", {
        method: "POST",
        body: JSON.stringify({
            query: query,
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    })
        .then((response) => response.json())
        .then((response) => {
            console.log(response);
            response.map((result, i) => {
                let path = result.path;
                resultBox.innerHTML += `
                    <li><a href="/api/results?${i}" data-path="${path}">${result.docName}</a></li>
                `
            });
        });
}

let input = document.body.querySelector("input");
input.addEventListener("keypress", e => {
    if(e.key === "Enter") {
        e.preventDefault();
        sendSearchRequest(input.value);
    }
})