function getUsers() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/users/";
    var usersList = "";
    $.ajax
    ({
        type: "GET",
        url: queryUrl,
        async: false,
        contentType: "application/json",
        beforeSend: function (xhr) {
            // set header if JWT is set
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " + sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            usersList = data;
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
    return usersList;
}


function addUser(authorities, author, langKey, email, firstName, lastName, username, password) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/users/";

    var data = {
            activated: true,
            authorities: authorities,
            createdBy: author,
            createdDate: new Date().getDate(),
            langKey: langKey,
            lastModifiedBy: author,
            lastModifiedDate: new Date().getDate(),
            email: email,
            firstName: firstName,
            lastName: lastName,
            login: username,
            password: password
        };
    $.ajax
    ({
        type: "POST",
        url: queryUrl,
        async: false,
        data: JSON.stringify(data),
        contentType: "application/json",
        beforeSend: function (xhr) {
            // set header if JWT is set
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " + sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            alert("User created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateUser() {

}

function changePassword(password) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/account/change-password";
    $.ajax
    ({
        type: "POST",
        url: queryUrl,
        async: false,
        data: password,
        contentType: "application/json",
        beforeSend: function (xhr) {
            // set header if JWT is set
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " + sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            return true;
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updatePersonalInformation(username,password,email,firstname,lastname) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/account";
    var data;
    if (password) {
        data = {
            email: email,
            firstName: firstname,
            lastName: lastname,
            login: username,
            password: password
        };
    }
    else {
        data = {
            email: email,
            firstName: firstname,
            lastName: lastname,
            login: username
        };
    }
    $.ajax
    ({
        type: "POST",
        url: queryUrl,
        async: false,
        data: JSON.stringify(data),
        contentType: "application/json",
        beforeSend: function (xhr) {
            // set header if JWT is set
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " + sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            alert("Settings saved correctly! If you changed the password, logout and then login with the new password.");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });

    changePassword(password);
}

function deleteUser(login) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/users/" + login;
    $.ajax
    ({
        type: "DELETE",
        url: queryUrl,
        async: false,
        contentType: "application/json",
        beforeSend: function (xhr) {
            // set header if JWT is set
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " + sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            alert("User deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}