$().ready(function () {
    $("#bankTransferTopUpForm").validate({
        rules: {
            amount: {
                required: true,
                number:true,
                minStrict: true
            },
        },
    });
    
    $("#bankTransferUseForm").validate({
        rules: {
            amount: {
                required: true,
                number:true,
                minStrict: true
            },
        },
    });
});

jQuery.validator.addMethod("minStrict", function(value, element) {
  return this.optional(element) || value > 0;
},"Please entre an amount greater than 0.");