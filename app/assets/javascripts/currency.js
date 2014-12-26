$(".test").each(function() {
    var el = $(this);
    el.css("margin-top", (el.parent().parent().height() - el.height()) / 2);
});

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

var CurCheck = $("#disclaimer-acceptance");
var CurSel = $("#sel-currency");
var CurBtn = $("#btn-currency");
var Values = $(".moneyValue");
//var ValuesBKP = Values.clone(true,true);



function from() {
    var val = Values.first().text().toLowerCase();
    if (val.endsWith('€')) {
        return "eur";
    } else if (val.endsWith("chf")) {
        return "chf";
    } else if (val.endsWith("dkk")) {
        return "dkk";
    } else if (val.endsWith("gbp")) {
        return "gbp";
    } else if (val.endsWith("nok")) {
        return "nok";
    } else if (val.endsWith("sek")) {
        return "sek";
    } else {
        return "usd";
    }

}

function to() {
    var res = CurSel.find(":selected").text();
    if (res == "Euro")
        return "eur";
    else
        return res;
}


CurCheck.bind("change", function() {
    CurSel.attr("disabled", !CurSel.attr("disabled"));
    CurBtn.attr("disabled", !CurBtn.attr("disabled"));
});

CurBtn.click(function() {
    $.ajax({
        url: "http://query.yahooapis.com/v1/public/yql?q=SELECT%20Rate%20FROM%20yahoo.finance.xchange%20WHERE%20pair%20%3D%20%22" + from() + to() + "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=",
        success: function(data) {
            var rate = parseFloat(data.query.results.rate.Rate);
            console.log(rate);
            Values.each(function() {
                var price = parseFloat($(this).text());
                var suffix = to();
                if (suffix == "eur")
                    suffix = "€";
                $(this).html((price * rate).toFixed(0) + " " + suffix);
            });
        }
    });
});



$(function(){
    var min = 0;
    $(".period-holder").each(function(){
        if($(this).outerHeight(true) > min)
            min = $(this).outerHeight(true);
        console.log(min);
    }).css("height", min+"px");
});