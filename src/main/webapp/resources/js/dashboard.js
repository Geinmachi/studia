function competitorHover(obj) {
    var classList = obj.className.split(/\s+/);

    for (var i = 0; i < classList.length; i++) {
        if (classList[i].indexOf("competitiorInMatch") > -1) {
            $("." + classList[i]).addClass("hoveredCompetitor");

            return;
        }
    }
}

function competitorOut(obj) {
    var classList = obj.className.split(/\s+/);

    for (var i = 0; i < classList.length; i++) {
        if (classList[i].indexOf("competitiorInMatch") > -1) {
            $("." + classList[i]).removeClass("hoveredCompetitor");

            return;
        }
    }
}
$(document).ready(function () {

    $("div[class*='competitiorInMatch']").each(function () {
        $(this).on("mouseenter", function () {
            competitorHover(this);
        });
    });

    $("div[class*='competitiorInMatch']").each(function () {
        $(this).on("mouseleave", function () {
            competitorOut(this);
        });
    });

});

$("div[id$='CompetitionForm\\:dashboard']").ready(function () {
    $(this).find(".ui-panel-titlebar").each(function () {
        $(this).removeClass("ui-panel-titlebar");
    });
});

$(document).on("keypress", "[id$='scoreInput']", function (event) {
    if (event.which === 13) { // enter
        event.preventDefault(); // wylacza akcje eventu
        var $saveButton = $(event.target).parent().find("button:first");
        $saveButton.click();
    }
});
//$(document).on("blur", "[id$='scoreInput']", function () {
//    $(this).parent().find("button:first").click();
//});