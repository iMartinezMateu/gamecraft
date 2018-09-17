function getReports() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/reports?size=65536";
    var reportList = "";
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
            reportList = data;
        }
    });
    return reportList;
}

function getReport(id) {
    return getReports().filter(
        function(data){ return data.id == id }
    )[0];
}

function deleteReport(reportId) {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/reports/" + reportId;

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


function deleteReportsAssociatedToPipeline(pipelineId) {

    var reports = getReports();

    jQuery.each(reports, function(i, report) {
        if (report.pipelineId == pipelineId) {
            var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftpipelinemanager/api/reports/" + report.id;

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

