function getTelegramBots() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttelegramnotificationmanager/api/telegram-bots";
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

function getTelegramBot(id) {
    return getTelegramBots().filter(
        function(data){ return data.id == id }
    )[0];
}

function addTelegramBot(telegramBotName, telegramBotDescription, telegramBotToken) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttelegramnotificationmanager/api/telegram-bots/";

    var data = {
        "telegramBotDescription": telegramBotDescription,
        "telegramBotEnabled": true,
        "telegramBotName": telegramBotName,
        "telegramBotToken": telegramBotToken
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
            alert("Telegram notificator created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateTelegramBot(telegramBotId, telegramBotName, telegramBotDescription, telegramBotEnabled, telegramBotToken) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttelegramnotificationmanager/api/telegram-bots/";

    var data = {
        "id": telegramBotId,
        "telegramBotDescription": telegramBotDescription,
        "telegramBotEnabled": telegramBotEnabled,
        "telegramBotName": telegramBotName,
        "telegramBotToken": telegramBotToken
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
            alert("Telegram notificator updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteTelegramBot(telegramBotId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecrafttelegramnotificationmanager/api/telegram-bots/" + telegramBotId;

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
            alert("Telegram notificator deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}