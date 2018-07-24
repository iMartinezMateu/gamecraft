function getIRCBots() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftircnotificationmanager/api/irc-bots";
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

function getIRCBot(id) {
    return getIRCBots().filter(
        function(data){ return data.id == id }
    )[0];
}

function addIRCBot(ircBotName, ircBotDescription, ircBotNickname, ircServerAddress, ircServerPort, ircServerSecuredProtocolEnabled) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftircnotificationmanager/api/irc-bots/";

    var data = {
        "ircBotDescription": ircBotDescription,
        "ircBotEnabled": true,
        "ircBotName": ircBotName,
        "ircBotNickname": ircBotNickname,
        "ircServerAddress": ircServerAddress,
        "ircServerPort": ircServerPort,
        "ircServerSecuredProtocolEnabled": ircServerSecuredProtocolEnabled
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
            alert("IRC notificator created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateIRCBot(ircBotId, ircBotName, ircBotDescription, ircBotEnabled, ircBotNickname, ircServerAddress, ircServerPort, ircServerSecuredProtocolEnabled) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftircnotificationmanager/api/irc-bots/";

    var data = {
        "id": ircBotId,
        "ircBotDescription": ircBotDescription,
        "ircBotEnabled": ircBotEnabled,
        "ircBotName": ircBotName,
        "ircBotNickname": ircBotNickname,
        "ircServerAddress": ircServerAddress,
        "ircServerPort": ircServerPort,
        "ircServerSecuredProtocolEnabled": ircServerSecuredProtocolEnabled
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
            alert("IRC notificator updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });



}

function deleteIRCBot(ircBotId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftircnotificationmanager/api/irc-bots/" + ircBotId;

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
            alert("IRC notificator deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}