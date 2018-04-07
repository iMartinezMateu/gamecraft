function getTeams() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftteam/api/teams/";
    var teamsList = "";
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
            teamsList = data;
        }
    });
    return teamsList;
}

function searchTeam() {

}

function addTeam() {

}

function updateTeam() {

}

function deleteTeam() {

}