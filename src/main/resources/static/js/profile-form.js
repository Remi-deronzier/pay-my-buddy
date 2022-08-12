$().ready(function () {
    $("#profileForm").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            userName: "required",
            firstName: "required",
            lastName: "required",
            phoneNumber: {
				frenchPhoneNumber: true
			}
        },
    });
});

jQuery.validator.addMethod("frenchPhoneNumber", function(value, element) {
  return this.optional(element) || /^(?:(?:\+|00)33|0)\s*[1-9](?:[\s.-]*\d{2}){4}$/.test(value);
},"Please entre a valid french phone number");
