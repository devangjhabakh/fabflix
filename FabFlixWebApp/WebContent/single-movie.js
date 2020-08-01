function getParameterByName(target){
	// Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleMovieResultData(resultData){
	console.log("handleMovieResultData: populating general movie information with Result data");
	let movieTitleElement = jQuery("#movieTitle");
	let movieInfoElement = jQuery(".jumbotron");
	
	movieTitleElement.html(resultData["title"]);
	
	movieInfoElement.append("<p>The id of the movie is " + resultData["id"] + "</p>");
	movieInfoElement.append("<p>The year of release of the movie is " + resultData["year"] + "</p>");
	movieInfoElement.append("<p>The director of the movie is " + resultData["director"] + "</p>");
	movieInfoElement.append("<p>The List of Genres in the movie is " + resultData["genreList"] + "</p>");
	
	let actorsHtml = "<p>The list of actors in the movie is: ";
	
	let actorsArray = resultData["starList"];
	actorsArray = actorsArray.split(",");
	let actorsIdArray = resultData["starIdList"];
	actorsIdArray = actorsIdArray.split(",");
	
	for(let i=0;i<actorsArray.length;++i){
		actorsHtml += '<a href="single-star.html?id=' + actorsIdArray[i] + '">' + actorsArray[i] + ", </a>";
	}
	
	actorsHtml += "</p>";
	
	movieInfoElement.append(actorsHtml);
}

let movieId = getParameterByName('id');
jQuery.ajax({
	datatype: "json",
	method: "GET",
	url: "api/single-movie?id=" + movieId,
	success: (resultData) => handleMovieResultData(resultData)
});