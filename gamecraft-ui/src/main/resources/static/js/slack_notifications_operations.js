function getSlackAccounts() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftslacknotificationmanager/api/slack-accounts";
    var botList = "";
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
            botList = data;
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
    return botList;
}

function getSlackAccount(id) {
    return getSlackAccounts().filter(
        function(data){ return data.id == id }
    )[0];
}

function addSlackAccount(slackAccountName,slackAccountDescription,slackAccountToken) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftslacknotificationmanager/api/slack-accounts/";

    var data = {
        "slackAccountDescription": slackAccountDescription,
        "slackAccountEnabled": true,
        "slackAccountName": slackAccountName,
        "slackAccountToken": slackAccountToken
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
            alert("Slack notificator created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateSlackAccount(slackAccountId, slackAccountName, slackAccountDescription, slackAccountEnabled, slackAccountToken) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftslacknotificationmanager/api/slack-accounts/";

    var data = {
        "id": slackAccountId,
        "slackAccountDescription": slackAccountDescription,
        "slackAccountEnabled": slackAccountEnabled,
        "slackAccountName": slackAccountName,
        "slackAccountToken": slackAccountToken
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
            alert("Slack notificator updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteSlackAccount(slackNotificatorId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftslacknotificationmanager/api/slack-accounts/" + slackNotificatorId;

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
            alert("Slack notificator deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}