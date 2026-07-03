const mealAjaxUrl = "profile/meals/";
const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function updateTableByData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

function updateTable() {
    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();

    var url = ctx.ajaxUrl;
    var params = [];
    if (startDate) params.push('startDate=' + startDate);
    if (endDate) params.push('endDate=' + endDate);
    if (startTime) params.push('startTime=' + startTime);
    if (endTime) params.push('endTime=' + endTime);

    if (params.length > 0) {
        url += 'filter?' + params.join('&');
    }

    $.get(url, updateTableByData);
}

function resetFilter() {
    $('#filterForm')[0].reset();
    updateTable();
}