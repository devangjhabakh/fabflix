/**
 * 
 */

function handleResultError(resultData){
	console.log("Handle Result Error");
	jQuery("#login_error_message").html(resultData);
}

function handleCaptchaSuccess(){
	console.log("Handle Result Success");
	window.location.replace("index.html");
}

function handleResultSuccess(resultData){
	jQuery.ajax({
		url: "api/login",
		method: "POST",
		data: resultData,
		success: () => handleCaptchaSuccess(),
		error: () => handleResultError()
	})
}

function formDataHandler(formEvent){
	formEvent.preventDefault();
	
	formData = jQuery("#login_form").serialize();
	
	console.log(formData);
	
	jQuery.ajax({
		url: "api/form-recaptcha",
		method: "POST",
		data: formData,
		success: () => handleResultSuccess(formData),
		error: (resultData) => handleResultError(resultData)
	})
}

jQuery("#login_form").submit((event) => formDataHandler(event));