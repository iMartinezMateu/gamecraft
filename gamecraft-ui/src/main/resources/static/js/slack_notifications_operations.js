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

function addSlackAccount() {

}

function updateSlackAccount() {

}

function deleteSlackAccount() {

}