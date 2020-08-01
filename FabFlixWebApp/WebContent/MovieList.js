/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

function formEventHandler(givenOffset = 0){
	console.log("getting data from movie list servlet");
	
	jQuery.ajax({
		method: "GET",
		url: "api/movieList?offset=" + givenOffset, 
		success: (resultData) => handleMovieListResult(resultData)
	});	
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieListResult(resultData){
	console.log("handle Movie Result: Populating Movie table from movie data");
	let movieListTableBodyElement = jQuery("#movieList_table_body");
	movieListTableBodyElement.empty();
	console.log(resultData);
	for(let i=0;i<resultData.length;++i){
		let rowHtml = "";
		rowHtml += "<tr>";
		rowHtml += "<th>" + '<a href="single-movie.html?id=' + resultData[i]["movie_id"] + '">' + resultData[i]["title"] + "</a>" + "</th>";
		rowHtml += "<th>" + resultData[i]["year"] + "</th>";
		rowHtml += "<th>" + resultData[i]["director"] + "</th>";
		rowHtml += "<th>"
		var genreIdArray = resultData[i]["genreIdList"];
		genreIdArray = genreIdArray.split(",");
		var genreList = resultData[i]["genreList"];
		genreArray = genreList.split(",");
		for(let j=0;j<genreIdArray.length;++j){
			let currHtml = "";
			currHtml += '<a href="single-genre.html?id=' + genreIdArray[j] + '">' + genreArray[j] + ", </a>";
			rowHtml += currHtml;
		}
		rowHtml += "</th>";
		rowHtml += "<th>"
		var starIdArray = resultData[i]["starIdList"];
		starIdArray = starIdArray.split(",");
		var starNameArray = resultData[i]["starList"];
		starNameArray = starNameArray.split(",");
		for(let j=0;j<starIdArray.length;++j){
			let currHtml = "";
			currHtml += '<a href="single-star.html?id=' + starIdArray[j] + '">' + starNameArray[j] + ", </a>";
			rowHtml += currHtml;
		}
		rowHtml += "</th>";
		rowHtml += "<th>" + resultData[i]["rating"] + "</th>";
		rowHtml += "<th>" + '<button type="button" id="' + resultData[i]["movie_id"] + '" class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off">' + 'Add to Cart' + '</button>' + "</th>";
		rowHtml += "</tr>";
		movieListTableBodyElement.append(rowHtml);
		jQuery("#"+resultData[i]["movie_id"]).click(movieAdder(resultData[i]["movie_id"]));
	}
}

function movieAdder(movie_id){
	console.log(movie_id);
	let addMovieToCart = () => {
		console.log("adding movie" + movie_id + "to cart!");
		jQuery.ajax({
			method: "GET",
			url: "api/addMovieToCart?movie_id=" + movie_id
		});
	}
	return addMovieToCart;
}

let offSet = 0;
jQuery.ajax({
	method: "GET",
	url: "api/movieList?offset=0",
	success: (resultData) => handleMovieListResult(resultData)
})
jQuery("#nextPage").click(() => {
	offSet++;
	formEventHandler(offSet);
});
jQuery("#prevPage").click(() => {
	if(offSet > 0)
		offSet--;
	formEventHandler(offSet);
});
jQuery("#sort_form").submit((formData) => {
	console.log("Preventing Default..");
	formData.preventDefault();
	console.log("form data sent: " + formData);
	jQuery.ajax({
		method: "POST",
		data: jQuery("#sort_form").serialize(),
		url: "api/movieSort",
		success: () => {
			jQuery.ajax({
				method: "GET",
				url: "api/movieList?offset=0",
				success: (resultData) => handleMovieListResult(resultData)
			})
		}
	});
});
