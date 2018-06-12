function getEmailAccounts() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftemailnotificationmanager/api/email-accounts";
    var accountList = "";
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
            accountList = data;
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
    return accountList;
}

function addEmailAccount(emailAccountDescription, emailAccountName, emailSmtpPassword, emailSmtpPort, emailSmtpServer, emailSmtpUseSSL, emailSmtpUsername) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftemailnotificationmanager/api/email-accounts/";

    var data = {
        "emailAccountDescription": emailAccountDescription,
        "emailAccountEnabled": true,
        "emailAccountName": emailAccountName,
        "emailSmtpPassword": emailSmtpPassword,
        "emailSmtpPort": emailSmtpPort,
        "emailSmtpServer": emailSmtpServer,
        "emailSmtpUseSSL": emailSmtpUseSSL,
        "emailSmtpUsername": emailSmtpUsername
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
            alert("E-mail notificator created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateEmailAccount(emailNotificatorId,emailAccountDescription, emailAccountEnabled, emailAccountName, emailSmtpPassword, emailSmtpPort, emailSmtpServer, emailSmtpUseSSL, emailSmtpUsername) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftemailnotificationmanager/api/email-accounts/";

    var data = {
        "id": emailNotificatorId,
        "emailAccountDescription": emailAccountDescription,
        "emailAccountEnabled": emailAccountEnabled,
        "emailAccountName": emailAccountName,
        "emailSmtpPassword": emailSmtpPassword,
        "emailSmtpPort": emailSmtpPort,
        "emailSmtpServer": emailSmtpServer,
        "emailSmtpUseSSL": emailSmtpUseSSL,
        "emailSmtpUsername": emailSmtpUsername
    };
    $.ajax
    ({
        type: "PUT",
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
            alert("E-mail notificator updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteEmailAccount(emailNotificatorId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftemailnotificationmanager/api/email-accounts/" + emailNotificatorId;

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
            alert("E-mail notificator deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}