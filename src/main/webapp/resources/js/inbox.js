function closeInbox() {
    $("#inboxForm").hide();
}

function toggleInbox() {
    $("#inboxForm").toggle();
}

function showInbox() {
    $("#inboxForm").show();
}

function followEvent(event, obj) { // without event object p:commandButton does not see event(???) and propagates event many times (75)
    $(obj).find("button[id$=followEventAction]").click();
}