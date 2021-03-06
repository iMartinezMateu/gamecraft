function getPipelines() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines?size=65536";
    var pipelinesList = "";
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
            pipelinesList = data;
        }
    });
    return pipelinesList;
}

function getPipeline(id) {
    return getPipelines().filter(
        function(data){ return data.id == id }
    )[0];
}

function addPipeline(pipelineName,pipelineDescription,pipelineProjectId,pipelineProjectName,pipelineDropboxAppKey,pipelineDropboxToken,pipelineEngineCompilerPath,pipelineEngineCompilerArguments,pipelineFtpAddress,pipelineFtpUsername,pipelineFtpPassword,pipelineFtpPort,pipelineNotificatorType,pipelineNotificatorDetails,
                     pipelinePublicationService,pipelineRepositoryAddress,pipelineRepositoryPassword,pipelineRepositoryType,pipelineRepositoryUsername,pipelineRepositoryBranch,pipelineScheduleCronJob,pipelineScheduleType,pipelineNotificatorRecipient) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines/";

    var data = {
        pipelineName: pipelineName,
        pipelineDescription: pipelineDescription,
        pipelineProjectId: pipelineProjectId,
        pipelineProjectName: pipelineProjectName,
        pipelineDropboxAppKey: pipelineDropboxAppKey,
        pipelineDropboxToken: pipelineDropboxToken,
        pipelineEngineCompilerArguments: pipelineEngineCompilerArguments,
        pipelineEngineCompilerPath: pipelineEngineCompilerPath,
        pipelineFtpAddress: pipelineFtpAddress,
        pipelineFtpPassword: pipelineFtpPassword,
        pipelineFtpPort: pipelineFtpPort,
        pipelineFtpUsername: pipelineFtpUsername,
        pipelineNotificatorDetails: pipelineNotificatorDetails,
        pipelineNotificatorType: pipelineNotificatorType,
        pipelinePublicationService: pipelinePublicationService,
        pipelineRepositoryAddress: pipelineRepositoryAddress,
        pipelineRepositoryPassword: pipelineRepositoryPassword,
        pipelineRepositoryType: pipelineRepositoryType,
        pipelineRepositoryUsername: pipelineRepositoryUsername,
        pipelineRepositoryBranch: pipelineRepositoryBranch,
        pipelineScheduleCronJob: pipelineScheduleCronJob,
        pipelineScheduleType: pipelineScheduleType,
        pipelineStatus: "IDLE",
        pipelineNotificatorRecipient: pipelineNotificatorRecipient
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
            alert("Pipeline created!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function updatePipeline(pipelineId, pipelineName,pipelineDescription,pipelineProjectId,pipelineProjectName,pipelineDropboxAppKey,pipelineDropboxToken,pipelineEngineCompilerPath,pipelineEngineCompilerArguments,pipelineFtpAddress,pipelineFtpUsername,pipelineFtpPassword,pipelineFtpPort,pipelineNotificatorType,pipelineNotificatorDetails,
                        pipelinePublicationService,pipelineRepositoryAddress,pipelineRepositoryPassword,pipelineRepositoryType,pipelineRepositoryUsername,pipelineRepositoryBranch,pipelineScheduleCronJob,pipelineScheduleType,pipelineStatus,pipelineNotificatorRecipient) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines/";

    var data = {
        id: pipelineId,
        pipelineName: pipelineName,
        pipelineDescription: pipelineDescription,
        pipelineProjectId: pipelineProjectId,
        pipelineProjectName: pipelineProjectName,
        pipelineDropboxAppKey: pipelineDropboxAppKey,
        pipelineDropboxToken: pipelineDropboxToken,
        pipelineEngineCompilerArguments: pipelineEngineCompilerArguments,
        pipelineEngineCompilerPath: pipelineEngineCompilerPath,
        pipelineFtpAddress: pipelineFtpAddress,
        pipelineFtpPassword: pipelineFtpPassword,
        pipelineFtpPort: pipelineFtpPort,
        pipelineFtpUsername: pipelineFtpUsername,
        pipelineNotificatorDetails: pipelineNotificatorDetails,
        pipelineNotificatorType: pipelineNotificatorType,
        pipelinePublicationService: pipelinePublicationService,
        pipelineRepositoryAddress: pipelineRepositoryAddress,
        pipelineRepositoryPassword: pipelineRepositoryPassword,
        pipelineRepositoryType: pipelineRepositoryType,
        pipelineRepositoryUsername: pipelineRepositoryUsername,
        pipelineRepositoryBranch: pipelineRepositoryBranch,
        pipelineScheduleCronJob: pipelineScheduleCronJob,
        pipelineScheduleType: pipelineScheduleType,
        pipelineStatus: pipelineStatus,
        pipelineNotificatorRecipient: pipelineNotificatorRecipient
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
            alert("Pipeline updated!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deletePipeline(pipelineId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines/" + pipelineId;

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
            alert("Pipeline deleted!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function executePipeline(pipelineId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines/" + pipelineId + "/execute";

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
            alert("Pipeline executed!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function stopPipeline(pipelineId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines/" + pipelineId + "/stop";

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
            alert("Pipeline stopped!");
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function deletePipelinesAssociatedToProject(projectId) {

    var pipelines = getPipelines();

    jQuery.each(pipelines, function(i, pipeline) {
        if (pipeline.pipelineProjectId == projectId) {
            var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/pipelines/" + pipeline.id;

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
                },
                error: function (data) {
                    alert(JSON.stringify(data));
                }
            });
        }
    });


}

