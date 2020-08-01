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

function handleStarResult(resultData){
	
	console.log("handleResult: populating star info from resultData");
	console.log(resultData);
	//Getting webpage elements to be mutated
	
	let starTitleElement = jQuery("#startitle");
	let starInfoElement = jQuery("#starinfo");
	
	starTitleElement.html(resultData["name"]);
	
	let infoHtml = "The actor " + resultData["name"] + " was born in " + resultData["YOB"] + " here are some of their movies:";
	starInfoElement.html(infoHtml);
	
	let starListElement = jQuery(".list-group");
	
	movieNameArray = resultData["movieTitleList"];
	movieNameArray = movieNameArray.split(",");
	movieIdArray = resultData["movieIdList"];
	movieIdArray = movieIdArray.split(",");
	for(let j = 0;j<movieIdArray.length;++j){
		let rowHtml = "";
		rowHtml += '<a href="single-movie.html?id=' + movieIdArray[j] + '" class="list-group-item list-group-item-action">' + movieNameArray[j] + "</a>"  
		starListElement.append(rowHtml);
	}
}

let starId = getParameterByName('id');

jQuery.ajax({
	dataType: "json",
	method: "GET",
	url: "api/single-star?id=" + starId,
	success: (resultData) => handleStarResult(resultData)
})