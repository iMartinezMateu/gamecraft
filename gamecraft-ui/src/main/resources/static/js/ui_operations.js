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
        $.each(users, function(i, item) {
            var tr = $('<tr>').append(
                $('<tr>'),
                $('<td>').text(item.id),
                $('<td>').text(item.login),
                $('<td>').text(item.firstName),
                $('<td>').text(item.lastName),
                $('<td>').text(item.email),
                $('<td>').text(item.authorities),
                $('<td>').text(item.createdDate)
            );
            $(".table").append(tr.wrap('<tr>').html());
        });
    });
}