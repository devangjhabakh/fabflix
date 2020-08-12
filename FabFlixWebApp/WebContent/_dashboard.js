/**
 * 
 */
function handleMetadata(metadata){
	
	console.log("Handling Metadata!");
	
	let metadataElement = jQuery("#metadata");
	let currHtml = "";
	
	for(let i=0;i<metadata.length;++i){
		currHtml = "";
		currHtml += "<h1>";
		currHtml += metadata[i]["tablename"];
		currHtml += "</h1>";
		currHtml += '<table class="table">';
		currHtml += '<thead class="thead-dark">';
		currHtml += '<tr>';
		currHtml += '<th scope="col">Attribute</th>';
		currHtml += '<th scope="col">Type</th>';
		currHtml += '</tr>';
		currHtml += '</thead>';
		currHtml += '<tbody>';
		for(let j=0;j<metadata[i]["schema"].length;++j){
			currHtml += '<tr>';
			currHtml += '<td>'+metadata[i]["schema"][j]["attribute"]+"</td>";
			currHtml += '<td>'+metadata[i]["schema"][j]["type"]+"</td>";
			currHtml += '</tr>';
		}
		currHtml += '</tbody>';
		currHtml += '</table>';
		metadataElement.append(currHtml);
	}
}

function handleStarInputStatus(formInformation){
	jQuery("#starinputmessage").html(formInformation);
}

function handleMovieInputStatus(formInformation){
	jQuery("#movieinputmessage").html(formInformation);
}

function handleStarInput(event){
	event.preventDefault();
	
	formData = jQuery("#star-input").serialize();
	
	jQuery.ajax({
		url: "api/add_star",
		method: "POST",
		data: formData,
		success: (successInformation) => handleStarInputStatus(successInformation),
		error: (errorInformation) => handleStarInputStatus(errorInformation)
	});
}

function handleMovieInput(event){
	event.preventDefault();
	
	formData = jQuery("#movie-input").serialize();
	
	jQuery.ajax({
		url: "add_movie/add",
		method: "POST",
		data: formData,
		success: (successInformation) => handleMovieInputStatus(successInformation),
		error: (errorInformation) => handleMovieInputStatus(errorInformation)
	});
}

function handleLoginSuccess(loginData){
	
	console.log("Login Succeeded!");
	
	jQuery.ajax({
		url: "api/table_metadata",
		method: "POST",
		success: (metadata) => handleMetadata(metadata)
	})
	
//	jQuery("#login-form-div").empty();
	
	let dashboardElement = jQuery("#login-dashboard");
	
//	let addedHtml = "";
//	
//	//Creating the form to input stars
//	addedHtml += '<form id="star-input" action="#" method="post">';
//	addedHtml += '<div class="form-group row">';
//	addedHtml += '<label for="email" class="col-sm-2 col-form-label">Name</label>';
//	addedHtml += '<div class="col-sm-10">';
//	addedHtml += '<input type="text" class="form-control" name="name" placeholder="Name" required>';
//	addedHtml += '</div>';
//	addedHtml += '</div>';
//	addedHtml += '<div class="form-group row">';
//	addedHtml += '<label for="password" class="col-sm-2 col-form-label">Date Of Birth</label>';
//	addedHtml += '<div class="col-sm-10">';
//	addedHtml += '<input type="date" class="form-control" name="dob" placeholder="Date Of Birth">';
//	addedHtml += '</div>';
//	addedHtml += '</div>';
//	addedHtml += '<div class="form-group row">';
//	addedHtml += '<div class="col-sm-10">';
//	addedHtml += '<button type="submit" class="btn btn-primary" id="submitButtonSearch">Submit</button>';
//	addedHtml += '</div>';
//	addedHtml += '</div>';
//	addedHtml += '<div id=starinputmessage></div>'
//	addedHtml += '</form>';
//	
//	dashboardElement.append(addedHtml);
//	
//	jQuery("#star-input").submit((event) => handleStarInput(event));
	
	//Creating the form to input movies
	addedHtml = "";
	
	addedHtml += '<form id="movie-input" action="#" method="post">';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<label for="email" class="col-sm-2 col-form-label">Title</label>';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<input type="text" class="form-control" name="title" placeholder="Title" required>';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<label for="year" class="col-sm-2 col-form-label">Year</label>';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<input type="number" class="form-control" name="year" placeholder="Year" required>';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<label for="director" class="col-sm-2 col-form-label">Director</label>';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<input type="text" class="form-control" name="director" placeholder="Director" required>';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<label for="star" class="col-sm-2 col-form-label">Star</label>';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<input type="text" class="form-control" name="star" placeholder="Star" required>';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<label for="starDOB" class="col-sm-2 col-form-label">Star Birth Year</label>';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<input type="number" class="form-control" name="starDOB" placeholder="Star Birth Year">';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<label for="email" class="col-sm-2 col-form-label">Genre</label>';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<input type="text" class="form-control" name="genre" placeholder="Genre" required>';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div class="form-group row">';
	addedHtml += '<div class="col-sm-10">';
	addedHtml += '<button type="submit" class="btn btn-primary" id="submitButtonSearch">Submit</button>';
	addedHtml += '</div>';
	addedHtml += '</div>';
	addedHtml += '<div id=movieinputmessage></div>'
	addedHtml += '</form>';
	
	dashboardElement.append(addedHtml);
	
	jQuery("#movie-input").submit((event) => handleMovieInput(event));
}

function handleLoginFailure(errorMessage){
	jQuery("#login_error").empty();
	jQuery("#login_error").html(errorMessage);
}

jQuery("#login_form").submit((event) => {
	event.preventDefault();
	console.log("Default prevented!");
	let formData = jQuery("#login_form").serialize();
	jQuery.ajax({
		url: "api/dashboard_login",
		method: "POST",
		data: formData,
		success: (successMessage) => {
			console.log("Success!");
			handleLoginSuccess();
		},
		error: (errorMessage) => handleLoginFailure(errorMessage)
	})
}) 
