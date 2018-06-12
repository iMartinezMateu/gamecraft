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

function addTeam(teamName, teamDescription) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/teams/";

    var data = {
        teamName: teamName,
        teamDescription: teamDescription,
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
            alert("Team created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateTeam(teamId, teamName, teamDescription) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/teams/";

    var data = {
        teamId: teamId,
        teamName: teamName,
        teamDescription: teamDescription,
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
            alert("Team updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteTeam(teamId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/teams/" + teamId;

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
            alert("Team deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function assignUser(userId, teamId) {

}

function deassignUser(userId, teamId) {

}