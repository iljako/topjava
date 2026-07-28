const mealAjaxUrl = "profile/meals/";

const localeMeta = document.querySelector('meta[name="locale"]');
const currentLocale = localeMeta ? localeMeta.content : 'ru';

if (typeof jQuery !== 'undefined' && jQuery.datetimepicker) {
    jQuery.datetimepicker.setLocale(currentLocale);
}

const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            return JSON.parse(stringData,
                function (key, value) {
                    return (key === 'dateTime') ? value.substring(0, 16).replace('T', ' ') : value;
                }
            );
        }
    }
});

$(function () {
    makeEditable({
        "columns": [
            {"data": "dateTime"},
            {"data": "description"},
            {"data": "calories"},
            {"render": renderEditBtn, "defaultContent": "", "orderable": false},
            {"render": renderDeleteBtn, "defaultContent": "", "orderable": false}
        ],
        "order": [[0, "desc"]],
        "createdRow": function (row, data, dataIndex) {
            $(row).attr("data-meal-excess", data.excess);
        }
    });

    const dateOptions = {
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d'
    };

    const timeOptions = {
        datepicker: false,
        format: 'H:i'
    };

    var startDate = $('#startDate');
    var endDate = $('#endDate');

    startDate.datetimepicker({
        ...dateOptions,
        onShow: function (ct) {
            this.setOptions({ maxDate: endDate.val() ? endDate.val() : false });
        }
    });

    endDate.datetimepicker({
        ...dateOptions,
        onShow: function (ct) {
            this.setOptions({ minDate: startDate.val() ? startDate.val() : false });
        }
    });

    var startTime = $('#startTime');
    var endTime = $('#endTime');

    startTime.datetimepicker({
        ...timeOptions,
        onShow: function (ct) {
            this.setOptions({ maxTime: endTime.val() ? endTime.val() : false });
        }
    });

    endTime.datetimepicker({
        ...timeOptions,
        onShow: function (ct) {
            this.setOptions({ minTime: startTime.val() ? startTime.val() : false });
        }
    });

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});