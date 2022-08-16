$().ready(function () {
    $("#loginForm").validate({
        rules: {
            username: "required",
            password: "required",
        },
    });
});
