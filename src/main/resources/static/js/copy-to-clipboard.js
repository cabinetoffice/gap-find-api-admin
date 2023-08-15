document.addEventListener("DOMContentLoaded", function() {
    const copyButton = document.getElementById("copyButton");
    const copyText = document.getElementById("apiKeyValue");

    copyButton.addEventListener("click", function() {
        const tempTextArea = document.createElement("textarea");
        tempTextArea.value = copyText.textContent;
        document.body.appendChild(tempTextArea);

        tempTextArea.select();
        document.execCommand("copy");

        document.body.removeChild(tempTextArea);
    });
 });