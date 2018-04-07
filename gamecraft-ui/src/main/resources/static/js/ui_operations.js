function loadNavbar() {
    $(document).ready(function () {
        $("div.navbar-frame").load(location.protocol + '//' + document.domain + ':' + location.port + "/shared/navbar.html");
    });
}

function checkAuthState() {
    if (!isAuthenticated()) {
        window.location.replace(location.protocol + '//' + document.domain + ':' + location.port);
    }
}

function fillUsersTable() {
    var users = getUsers();
    $(function() {
        var role= "";
        $.each(users, function(i, item) {
            if (item.authorities[1] === "ROLE_ADMIN") {
                role = "<span class=\"badge badge-pill badge-primary\">ADMIN</span>";
            }
            if (item.authorities[1] == undefined) {
                role = "<span class=\"badge badge-pill badge-secondary\">USER</span>";
            }
            var tr = $('<tr>').append(
                $('<tr>'),
                $('<td>').text(item.id),
                $('<td>').text(item.login),
                $('<td>').text(item.firstName),
                $('<td>').text(item.lastName),
                $('<td>').text(item.email),
                $('<td>').html(role),
                $('<td>').text(item.createdDate)
            );
            $(".table").append(tr.html());
        });
    });
}

function fillTeamsTable() {
    var teams = getTeams();
    $(function() {
        $.each(teams, function(i, item) {
            var tr = $('<tr>').append(
                $('<tr>'),
                $('<td>').text(item.id),
                $('<td>').text(item.teamName),
                $('<td>').text(item.teamDescription)
            );
            $(".table").append(tr.html());
        });
    });
}