const mealAjaxUrl = "profile/meals/";

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
    ctx.updateTable();
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace('T', ' ');
                        }
                        return data;
                    }
                },
                {"data": "description"},
                {"data": "calories"},
                {"orderable": false, "defaultContent": "", "render": renderEditBtn},
                {"orderable": false, "defaultContent": "", "render": renderDeleteBtn}
            ],
            "order": [[0, "desc"]],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i',
        step: 10
    });
    $('#startDate, #endDate').datetimepicker({
        format: 'Y-m-d',
        timepicker: false
    });
    $('#startTime, #endTime').datetimepicker({
        format: 'H:i',
        datepicker: false
    });
});