function closeInbox() {
    $("#inboxForm").hide();
}

function toggleInbox() {
    if (checkDownloadToggleState()) {
        closeDownload();
    }
    $("#inboxForm").toggle();
}

function showInbox() {
    closeDownload();
    $("#inboxForm").show();
}

function followEvent(event, obj) { // without event object p:commandButton does not see event(???) and propagates event many times (75)
    $(obj).find("button[id$=followEventAction]").click();
}

function checkInboxToggleState() {
    if ($("#inboxForm").is(":hidden")) {
        return false;
    } else {
        return true;
    }
}

//DOWNLOAD SECTION

function closeDownload() {
    $("#downloadBlock").hide();
}

function toggleDownload() {
    if (checkInboxToggleState) {
        closeInbox();
    }
    $("#downloadBlock").toggle();
}

function showDownload() {
    closeInbox();
    $("#downloadBlock").show();
}

function checkDownloadToggleState() {
    if ($("#downloadBlock").is(":hidden")) {
        return false;
    } else {
        return true;
    }
}