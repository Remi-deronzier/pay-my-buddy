$().ready(function () {
    $("#externalAccountForm").validate({
        rules: {
            label: {
                required: true,
            },
        },
    });
});