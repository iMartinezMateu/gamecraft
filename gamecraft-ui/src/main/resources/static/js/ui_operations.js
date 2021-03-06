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
            if (isAdmin(getUsername())) {
                if (item.login == getUsername()) {
                    var tr = $('<tr>').append(
                        $('<tr>'),
                        $('<td>').text(item.id),
                        $('<td>').text(item.login),
                        $('<td>').text(item.firstName),
                        $('<td>').text(item.lastName),
                        $('<td>').text(item.email),
                        $('<td>').html(role),
                        $('<td>').text(item.createdDate),
                        $('<td>').html("")
                    );
                    $(".table").append(tr.html());
                }
                else {
                    var tr = $('<tr>').append(
                        $('<tr>'),
                        $('<td>').text(item.id),
                        $('<td>').text(item.login),
                        $('<td>').text(item.firstName),
                        $('<td>').text(item.lastName),
                        $('<td>').text(item.email),
                        $('<td>').html(role),
                        $('<td>').text(item.createdDate),
                        $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteUser('" + item.id + "'); location.reload(); \" ><span class=\"glyphicon glyphicon-remove\"></span> </button> <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-toggle=\"modal\" data-user-id=\"" + item.id +"\" data-target=\"#updateUserModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                    );
                    $(".table").append(tr.html());

                }
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.login),
                    $('<td>').text(item.firstName),
                    $('<td>').text(item.lastName),
                    $('<td>').text(item.email),
                    $('<td>').html(role),
                    $('<td>').text(item.createdDate),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }


        });
    });
}

function fillTeamsTable() {
    var teams = getTeams();
    $(function() {
        $.each(teams, function(i, item) {
            if (isAdmin(getUsername())) {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.teamName),
                    $('<td>').text(item.teamDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteTeam('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-team-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateTeamModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.teamName),
                    $('<td>').text(item.teamDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });

    });
}

function fillProjectsTable() {
    var projects = getProjects();
    $(function() {
        $.each(projects, function(i, item) {
            if (isAdmin(getUsername())) {

                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.projectName),
                    $('<td>').text(item.projectDescription),
                    $('<td>').html("<a href=" + item.projectWebsite + ">" + item.projectWebsite + "</a>"),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deletePipelinesAssociatedToProject('" + item.id + "'); deleteProject('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-project-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateProjectModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>   <a href='/pipelines?project_id=" + item.id + "'><button type=\"button\" class=\"btn btn-secondary btn-xs\"><span class=\"glyphicon glyphicon-tasks\"></span> </button></a>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.projectName),
                    $('<td>').text(item.projectDescription),
                    $('<td>').html("<a href=" + item.projectWebsite + ">" + item.projectWebsite + "</a>"),
                    $('<td>').html("  <a href='/pipelines?project_id=" + item.id + "'><button type=\"button\" class=\"btn btn-secondary btn-xs\"><span class=\"glyphicon glyphicon-tasks\"></span> </button></a>")
                );
                $(".table").append(tr.html());
            }
        });

    });
}

function fillPipelinesTable(projectId) {
    var pipelines = getPipelines();
    $(function() {
        $.each(pipelines, function(i, item) {
            if (item.pipelineProjectId == projectId) {
                if (isAdmin(getUsername())) {

                    var tr = $('<tr>').append(
                        $('<tr>'),
                        $('<td>').text(item.id),
                        $('<td>').text(item.pipelineName),
                        $('<td>').text(item.pipelineDescription),
                        $('<td>').text(item.pipelineStatus),
                        $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deletePipeline('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-pipeline-id=\"" + item.id + "\" data-toggle=\"modal\" data-target=\"#updatePipelineModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>  <button type=\"button\" class=\"btn btn-success btn-xs\" onclick=\" \"><span class=\"glyphicon glyphicon-play\" onclick=\"executePipeline('" + item.id + "'); location.reload(); \"></span>  </button> <button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"stopPipeline('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-pause\"></span> <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-pipeline-id=\"" + item.id + "\" data-toggle=\"modal\" data-target=\"#reportsModal\" onclick=\"\"><span class=\"glyphicon glyphicon-list-alt\"></span> </button>   ")
                    );
                    $(".table").append(tr.html());
                }
                else {
                    var tr = $('<tr>').append(
                        $('<tr>'),
                        $('<td>').text(item.id),
                        $('<td>').text(item.pipelineName),
                        $('<td>').text(item.pipelineDescription),
                        $('<td>').text(item.pipelineStatus),
                        $('<td>').html("<button type=\"button\" class=\"btn btn-success btn-xs\" onclick=\" \"><span class=\"glyphicon glyphicon-play\" onclick=\"executePipeline('" + item.id + "'); location.reload();\"></span> </button> <button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"stopPipeline('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-pause\"></span> <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-pipeline-id=\"" + item.id + "\" data-toggle=\"modal\" data-target=\"#reportsModal\" onclick=\"\"><span class=\"glyphicon glyphicon-list-alt\"></span> ")
                    );
                    $(".table").append(tr.html());
                }
            }
        });

    });
}

function fillEnginesTable() {
    var engines = getEngines();
    $(function() {
        $.each(engines, function(i, item) {
            if (isAdmin(getUsername())) {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.engineName),
                    $('<td>').text(item.engineDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteEngine('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-engine-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateEngineModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.engineName),
                    $('<td>').text(item.engineDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });
    });
}

function fillEmailAccountsTable() {
    var accounts = getEmailAccounts();
    $(function() {
        $.each(accounts, function(i, item) {
            if (isAdmin(getUsername())) {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.emailAccountName),
                    $('<td>').text(item.emailAccountDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteEmailAccount('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\"  data-notificator-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateNotificatorModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.emailAccountName),
                    $('<td>').text(item.emailAccountDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });
    });
}

function fillIRCBotsTable() {
    var bots = getIRCBots();
    $(function() {
        $.each(bots, function(i, item) {
            if (isAdmin(getUsername())) {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.ircBotName),
                    $('<td>').text(item.ircBotDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteIRCBot('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\"  data-notificator-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateNotificatorModal\"onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.ircBotName),
                    $('<td>').text(item.ircBotDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });
    });
}

function fillSlackAccountTable() {
    var bots = getSlackAccounts();
    $(function() {
        $.each(bots, function(i, item) {
            if (isAdmin(getUsername())) {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.slackAccountName),
                    $('<td>').text(item.slackAccountDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteSlackAccount('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-notificator-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateNotificatorModal\"  onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.slackAccountName),
                    $('<td>').text(item.slackAccountDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });

    });
}

function fillTelegramBotsTable() {
    var bots = getTelegramBots();
    $(function() {
        $.each(bots, function(i, item) {
            if (isAdmin(getUsername())) {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.telegramBotName),
                    $('<td>').text(item.telegramBotDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteTelegramBot('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-notificator-id=\"" + item.id +"\" data-toggle=\"modal\" data-target=\"#updateNotificatorModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button>")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.telegramBotName),
                    $('<td>').text(item.telegramBotDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });
    });
}

function fillTwitterBotsTable() {
    var bots = getTwitterBots();
    $(function() {
        $.each(bots, function(i, item) {
            if (isAdmin(getUsername())) {

                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.twitterBotName),
                    $('<td>').text(item.twitterBotDescription),
                    $('<td>').html("<button type=\"button\" class=\"btn btn-danger btn-xs\" onclick=\"deleteTwitterBot('" + item.id + "'); location.reload(); \"><span class=\"glyphicon glyphicon-remove\"></span> </button>  <button type=\"button\" class=\"btn btn-secondary btn-xs\" data-toggle=\"modal\" data-notificator-id=\"" + item.id +"\" data-target=\"#updateNotificatorModal\" onclick=\"\"><span class=\"glyphicon glyphicon-pencil\"></span> </button> ")
                );
                $(".table").append(tr.html());
            }
            else {
                var tr = $('<tr>').append(
                    $('<tr>'),
                    $('<td>').text(item.id),
                    $('<td>').text(item.twitterBotName),
                    $('<td>').text(item.twitterBotDescription),
                    $('<td>').html("")
                );
                $(".table").append(tr.html());
            }
        });
    });
}

function fillSettingsForm(username) {
    var accountInformation = getAccountInformation(username);
    $(document).ready(function () {
        document.getElementById('firstname').value = accountInformation.firstName;
        document.getElementById('lastname').value = accountInformation.lastName;
        document.getElementById('email').value = accountInformation.email;
        document.getElementById('language').value = localStorage.getItem("language").toLowerCase();
    });

}

function findGetParameter(parameterName) {
    var result = null,
        tmp = [];
    location.search
        .substr(1)
        .split("&")
        .forEach(function (item) {
            tmp = item.split("=");
            if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
        });
    return result;
}