function saveSettings(username, password, email, firstname, lastname, language) {


    if (password.length > 0 && password.length < 6) {
        alert("Password should be at least 6 characters long");
    }
    else {
        localStorage.setItem("language", language);
        updatePersonalInformation(username, password, email, firstname, lastname);
    }

}