var from = $("#from");
var to = $("#to");
var submit = $("#submit");
var hidden = $("#nonref");
var price = $("#priceLabel");
var discountDiv = $("#hidden-discount");
var accept = $("#acceptDiscount");

Date.prototype.addDays = function(days) {
    var dat = new Date(this.valueOf());
    dat.setDate(dat.getDate() + days);
    return dat;
};

$(function() {
    from.datepicker({
        dateFormat: "dd-mm-yy",
        minDate: 0
    });
    to.datepicker({
        dateFormat: "dd-mm-yy",
        minDate: from.datepicker("option", "minDate") + 1
    });
});

from.change(function() {
    to.datepicker("option", "minDate", from.datepicker("getDate").addDays(1));
    showModal();
    //showDiscount();
});

to.change(function() {
    from.datepicker("option", "maxDate", to.datepicker("getDate").addDays(-1));
    showModal();
    //showDiscount();
});

function showDiscount() {
    var dateFrom = from.datepicker("getDate");
    var dateTo = to.datepicker("getDate");
    var tmp = from.datepicker("getDate");
    tmp = tmp.addDays(3);

    if (dateTo > tmp && dateFrom !== null && dateTo !== null) {
        discountDiv.removeClass("hidden");
    } else {
        discountDiv.addClass("hidden");
    }
}

accept.click(function() {
    //console.log("click");
    hideModal();
    //showDiscount();
});


function hideModal() {
    if (price.attr("updated") === null) {
        var priceVal = parseFloat(price.text());
        price.text((priceVal * 0.9).toFixed(2));
        price.attr("updated", "true");
        hidden.val("true");
    }

    $('#myModal').modal("hide");
}


function showModal() {
    var dateFrom = from.datepicker("getDate");
    var dateTo = to.datepicker("getDate");
    var tmp = from.datepicker("getDate");
    tmp = tmp.addDays(3);
    if (dateTo > tmp && dateFrom !== null && dateTo !== null) {
        $('#myModal').modal();
    }
}