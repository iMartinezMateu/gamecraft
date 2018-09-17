function getEngines() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftenginemanager/api/engines?size=65536";
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

function getEngine(id) {
    return getEngines().filter(
        function(data){ return data.id == id }
    )[0];
}

function addEngine(engineName, engineDescription, engineCompilerPath, engineCompilerArguments) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftenginemanager/api/engines/";

    var data = {
        engineCompilerArguments: engineCompilerArguments,
        engineCompilerPath: engineCompilerPath,
        engineDescription: engineDescription,
        engineName: engineName
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
            alert("Engine created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updateEngine(engineId, engineName, engineDescription, engineCompilerPath, engineCompilerArguments) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftenginemanager/api/engines/";

    var data = {
        id: engineId,
        engineCompilerArguments: engineCompilerArguments,
        engineCompilerPath: engineCompilerPath,
        engineDescription: engineDescription,
        engineName: engineName
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
            alert("Engine updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deleteEngine(engineId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftenginemanager/api/engines/" + engineId;

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
            alert("Engine deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}