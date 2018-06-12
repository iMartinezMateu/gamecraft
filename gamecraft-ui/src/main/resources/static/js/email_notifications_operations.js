function getEmailAccounts() {
    var queryUrl = location.protocol + '//' + document.domain + ":8080/gamecraftemailnotificationmanager/api/email-accounts";
    var accountList = "";
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
            accountList = data;
        },
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
    return accountList;
}

function addEmailAccount() {

}

function updateEmailAccount() {

}

function deleteEmailAccount() {

}