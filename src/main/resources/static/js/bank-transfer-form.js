$().ready(function () {
    $("#bankTransferTopUpForm").validate({
        rules: {
            amount: {
                required: true,
                number:true,
                min: 10 
            },
        },
    });
    
    $("#bankTransferUseForm").validate({
        rules: {
            amount: {
                required: true,
                number:true,
                min: 10 
            },
        },
    });
});