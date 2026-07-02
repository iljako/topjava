const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function updateEnabled(checkbox, id) {
    var enabled = checkbox.checked;
    $.ajax({
        url: userAjaxUrl + id + '/enable?enabled=' + enabled,
        type: "PATCH"
    }).done(function () {
        checkbox.closest('tr').toggleClass('disabled-user', !enabled);
        successNoty("Updated");
    }).fail(function (jqXHR) {
        checkbox.checked = !enabled;
        failNoty(jqXHR);
    });
}