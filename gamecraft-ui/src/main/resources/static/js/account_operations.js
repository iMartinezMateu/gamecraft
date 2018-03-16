function authenticate(username, password) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/authenticate";
    if (!isEmpty(username) || !isEmpty(password) ) {
        $.ajax
        ({
            type: "POST",
            url: queryUrl,
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify({ "username": username, "password" : password , "rememberMe" : false}),
            success: function (data) {
                sessionStorage.setItem("token", data["id_token"]);
                if (isAuthenticated()) {
                    window.location.replace(location.protocol + '//' + document.domain + ':' + location.port + "/dashboard");
                }
            },
            error: function () {
                alert("There was an error in the authentication process. Please, try again...");
            }
        })
    }
}

function isAuthenticated() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/authenticate";
    var authFlag = false;
    $.ajax
    ({
        type: "GET",
        url: queryUrl,
        async : false,
        beforeSend : function(xhr) {
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " +  sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Content-Type': 'application/json'
        },
        success: function () {
             authFlag = true;
        },
        error: function () {
            authFlag = false;
        }
    });

    return authFlag;
}

function getAccountInformation(username) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/users/" + username;
    $.ajax
    ({
        type: "GET",
        url: queryUrl,
        contentType: "application/json",
        beforeSend : function(xhr) {
            // set header if JWT is set
            if (sessionStorage.getItem("token")) {
                xhr.setRequestHeader("Authorization", "Bearer " +  sessionStorage.getItem("token"));
            }
        },
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            alert(data);
        }
    })
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}