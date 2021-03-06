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
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftteam/api/teams/";

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
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftteam/api/teams/";

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
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftteam/api/teams/" + teamId;

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
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftteam/api/team-users/";

    var data = {
        teamId: teamId,
        userId: userId,
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
            alert("User assigned to the team!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deassignUser(assignmentId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/gamecraftteam/team-users/" + assignmentId;

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
            alert("Assignment deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function assignProject(teamId, projectId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/gamecraftteam/team-projects/";

    var data = {
        teamId: teamId,
        projectId: projectId,
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
            alert("Project assigned to the team!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deassignProject(assignmentId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/gamecraftteam/team-projects/" + assignmentId;

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
            alert("Assignment deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}