function getProjects() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftproject/api/projects?size=65536";
    var projectsList = "";
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
            projectsList = data;
        }
    });
    return projectsList;
}

function getProject(id) {
    return getProjects().filter(
        function(data){ return data.id == id }
    )[0];
}

function addProject(projectName, projectDescription, projectWebsite) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftproject/api/projects/";

    var data = {
        projectName: projectName,
        projectDescription: projectDescription,
        projectWebsite: projectWebsite,
        projectArchived: false,
        projectLogo: ""
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
            alert("Project created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateProject(projectId, projectName, projectDescription, projectWebsite, projectArchived) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftproject/api/projects/";

    var data = {
        id: projectId,
        projectName: projectName,
        projectDescription: projectDescription,
        projectWebsite: projectWebsite,
        projectArchived: projectArchived,
        projectLogo: ""
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
            alert("Project updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteProject(projectId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftproject/api/projects/" + projectId;

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
            alert("Project deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

