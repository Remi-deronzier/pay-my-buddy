$().ready(function () {
    $("#signupForm").validate({
        rules: {
            email: {
				required: true,
				email: true
			},
            userName: "required",
            firstName: "required",
            lastName: "required",
            pheenNumber: "required",
            password: "required",
            passwordConfirmation: {
				required: true,
				equalTo: "#password"
			}
            
        },
    });
});