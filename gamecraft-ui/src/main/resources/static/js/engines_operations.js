function getEngines() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftenginemanager/api/engines/";
    var enginesList = "";
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
            enginesList = data;
        }
    });
    return enginesList;
}

function searchEngine() {

}

function addEngine() {

}

function updateEngine() {

}

function deleteEngine() {

}