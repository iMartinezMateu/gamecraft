function getProjects() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftproject/api/projects/";
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

function searchProject() {

}

function addProject() {

}

function updateProject() {

}

function deleteProject() {

}