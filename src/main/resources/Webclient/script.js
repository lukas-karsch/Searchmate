function sendRequest(query) {
    console.log("Requesting...");
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
            response.map(result => {
                resultBox.innerHTML += `
                    <li><a href="${result.path}">${result.docName}</a></li>
                `
            })
        }); //this link does not work?
}

let input = document.body.querySelector("input");
input.addEventListener("keypress", e => {
    if(e.key === "Enter") {
        e.preventDefault();
        sendRequest(input.value);
    }
})