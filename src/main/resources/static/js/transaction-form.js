$().ready(function () {
    $("#transactionForm").validate({
        rules: {
            amount: {
                required: true,
                number:true,
                min: 10 
            },
        },
    });
});