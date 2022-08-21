$().ready(function () {
    $("#resetPasswordForm").validate({
        rules: {
            password: "required",
            passwordConfirmation: {
				required: true,
				equalTo: "#password"
			}
        },
    });
});