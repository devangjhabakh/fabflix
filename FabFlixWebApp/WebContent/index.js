/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

function searchFormEventHandler(formInput){
	formInput.preventDefault();
	givenQuery = formInput;
	
	formData = jQuery("#search_form").serialize();
	
	console.log(formData);
	
	jQuery.ajax({
		method: "POST",
		url: "api/movieListSearch",
		data: formData,
		success: (resultData) => sendToMovieList()
	});	
}

function browseFormGenreEventHandler(formInput){
	formInput.preventDefault();
	givenQuery = formInput;
	
	formData = jQuery("#browse_form_genre").serialize();
	
	console.log(formData);
	
	jQuery.ajax({
		method: "POST",
		url: "api/browse",
		data: formData,
		success: (resultData) => sendToMovieList()
	});	
}

function browseFormLetterEventHandler(formInput){
	formInput.preventDefault();
	givenQuery = formInput;
	
	formData = jQuery("#browse_form_letter").serialize();
	
	console.log(formData);
	
	jQuery.ajax({
		method: "POST",
		url: "api/browse",
		data: formData,
		success: (resultData) => sendToMovieList()
	});	
}

function sendToMovieList(){
	window.location.replace("MovieList.html");
}

function createGenreList(resultData){
	let genreListElement = jQuery("#genreList");
	for(let i=0;i<resultData.length;++i){
		let rowHtml = '<option value="' + resultData[i]["genre"] + '">' + resultData[i]["genre"] + "</option>";
		genreListElement.append(rowHtml);
	}
}

function createLetterList(resultData){
	let letterListElement = jQuery("#startingLetter");
	for(let i=0;i<resultData.length;++i){
		let rowHtml = '<option value="' + resultData[i]["startingLetter"] + '">' + resultData[i]["startingLetter"] + "</option>";
		letterListElement.append(rowHtml);
	}
}

jQuery.ajax({
	method: "POST",
	url: "api/genreList",
	success: (resultData) => createGenreList(resultData)
})

jQuery.ajax({
	method: "POST",
	url: "api/letterList",
	success: (resultData) => createLetterList(resultData)
})

jQuery("#search_form").submit((event) => searchFormEventHandler(event));
jQuery("#browse_form_genre").submit((event) => browseFormGenreEventHandler(event));
jQuery("#browse_form_letter").submit((event) => browseFormLetterEventHandler(event));