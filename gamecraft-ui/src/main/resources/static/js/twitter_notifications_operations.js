function getTwitterBots() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttwitternotificationmanager/api/twitter-bots";
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

function getTwitterBot(id) {
    return getTwitterBots().filter(
        function(data){ return data.id == id }
    )[0];
}

function addTwitterBot(twitterBotName, twitterBotDescription, twitterBotConsumerKey, twitterBotConsumerSecret, twitterBotAccessToken, twitterBotAccessTokenSecret) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttwitternotificationmanager/api/twitter-bots/";

    var data = {
        "twitterBotAccessToken": twitterBotAccessToken,
        "twitterBotAccessTokenSecret": twitterBotAccessTokenSecret,
        "twitterBotConsumerKey": twitterBotConsumerKey,
        "twitterBotConsumerSecret": twitterBotConsumerSecret,
        "twitterBotDescription": twitterBotDescription,
        "twitterBotEnabled": true,
        "twitterBotName": twitterBotName
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
            alert("Twitter notificator created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateTwitterBot(twitterBotId,twitterBotName, twitterBotDescription, twitterBotEnabled, twitterBotConsumerKey, twitterBotConsumerSecret, twitterBotAccessToken, twitterBotAccessTokenSecret) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttwitternotificationmanager/api/twitter-bots/";

    var data = {
        "id": twitterBotId,
        "twitterBotAccessToken": twitterBotAccessToken,
        "twitterBotAccessTokenSecret": twitterBotAccessTokenSecret,
        "twitterBotConsumerKey": twitterBotConsumerKey,
        "twitterBotConsumerSecret": twitterBotConsumerSecret,
        "twitterBotDescription": twitterBotDescription,
        "twitterBotEnabled": twitterBotEnabled,
        "twitterBotName": twitterBotName
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
            alert("Twitter notificator updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteTwitterBot(twitterBotId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttwitternotificationmanager/api/twitter-bots/" + twitterBotId;

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
            alert("Twitter notificator deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}