function setDefaultLanguage() {
    if (!localStorage.getItem("language")) {
        localStorage.setItem("language","EN");
    }
}

function getLanguage() {
    return localStorage.getItem("language");
}