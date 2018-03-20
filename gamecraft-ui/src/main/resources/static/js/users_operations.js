function getUsers() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/api/users/";
    var usersList = "";
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
            usersList = data;
        }
    });
    return usersList;
}

function searchUser() {

}

function addUser() {

}

function updateUser() {

}

function deleteUser() {

}