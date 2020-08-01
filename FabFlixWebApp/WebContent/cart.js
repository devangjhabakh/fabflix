/**
 * 
 */

function serveCartDetails(resultData){
	let cartTableBody = jQuery("#cartList_table_body");
	cartTableBody.empty();
	for(let i=0;i<resultData.length;++i){
		let rowHtml = "";
		rowHtml += "<tr>";
		rowHtml += "<th>" + resultData[i]["title"] + "</th>";
		rowHtml += "<th>" + resultData[i]["quantity"] + "</th>";
		rowHtml += "<th>" + '<button type="button" id="' + resultData[i]["movie_id"] + 'Add" class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off">' + '+' + '</button>' + "</th>";
		rowHtml += "<th>" + '<button type="button" id="' + resultData[i]["movie_id"] + 'Subtract" class="btn btn-primary" data-toggle="button" aria-pressed="false" autocomplete="off">' + '-' + '</button>' + "</th>";
		rowHtml += "</tr>";
		cartTableBody.append(rowHtml);
		jQuery("#" + resultData[i]["movie_id"] + "Add").click(createAdder(resultData[i]["movie_id"]));
		jQuery("#" + resultData[i]["movie_id"] + "Subtract").click(createSubtractor(resultData[i]["movie_id"]));
	}
}

function createSubtractor(movie_id){
	let subtractor = () => {
		jQuery.ajax({
			url: "api/subtractor?movie_id=" + movie_id,
			method: "GET",
			success: () => {
				jQuery.ajax({
					url: "api/cart",
					method: "POST",
					success: (resultData) => serveCartDetails(resultData)
				});
			}
		});
	}
	return subtractor;
}

function createAdder(movie_id){
	let adder = () => {
		jQuery.ajax({
			url: "api/adder?movie_id=" + movie_id,
			method: "GET",
			success: () => {
				jQuery.ajax({
					url: "api/cart",
					method: "POST",
					success: (resultData) => serveCartDetails(resultData)
				});
			}
		});
	}
	return adder;
}

jQuery.ajax({
	url: "api/cart",
	method: "POST",
	success: (resultData) => serveCartDetails(resultData)
});