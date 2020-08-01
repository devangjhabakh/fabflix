/**
 * 
 */

function handleResultError(resultData){
	console.log("Handle Result Error");
	console.log(resultData);
}

function handleResultSuccess(resultData){
	console.log("Handle Result Success");
	window.location.replace("index.html");
}

function formDataHandler(formEvent){
	formEvent.preventDefault();
	
	formData = jQuery("#login_form").serialize();
	
	console.log(formData);
	
	jQuery.ajax({
		url: "api/login",
		method: "POST",
		data: formData,
		success: (resultData) => handleResultSuccess(resultData),
		error: (resultData) => handleResultError(resultData)
	})
}

jQuery("#login_form").submit((event) => formDataHandler(event));