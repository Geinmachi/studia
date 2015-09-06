function competitorHover(obj) {
    var classList = obj.className.split(/\s+/);

    for (var i = 0; i < classList.length; i++) {
        console.log("Klasa " + classList[i]);
        if (classList[i].indexOf("competitiorInMatch") > -1) {
            $("." + classList[i]).addClass("hoveredCompetitor");

            return;
        }
    }
}

function competitorOut(obj) {
    var classList = obj.className.split(/\s+/);

    for (var i = 0; i < classList.length; i++) {
        console.log("Klasa " + classList[i]);
        if (classList[i].indexOf("competitiorInMatch") > -1) {
            $("." + classList[i]).removeClass("hoveredCompetitor");

            return;
        }
    }
}
$(document).ready(function () {

    $("div[class*='competitiorInMatch']").each(function () {
        $(this).on("mouseenter", function(){
            competitorHover(this);
        });
    });
    
    $("div[class*='competitiorInMatch']").each(function () {
        $(this).on("mouseleave", function(){
            competitorOut(this);
        });
    });
});

