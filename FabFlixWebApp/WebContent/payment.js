/**
 * 
 */

jQuery("#payment_form").submit((formData) => {
	formData.preventDefault();
	jQuery.ajax({
		method: "POST",
		data: jQuery("#payment_form").serialize(),
		url: "payment/pay",
		success: () => {
			window.location.replace("confirmation.html");
		}
	})
})