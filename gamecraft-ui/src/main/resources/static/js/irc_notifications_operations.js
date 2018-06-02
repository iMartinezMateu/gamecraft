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

function searchIRCBot() {

}

function addIRCBot() {

}

function updateIRCBot() {

}

function deleteIRCBot() {

}