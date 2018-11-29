$(function() {
	$("form").submit(function(event) {
		event.preventDefault();
		$("#form-status").html("Searching..");
		console.log("get grouped blogs");
		let formArr = $(this).serializeArray();
		let json = formToJson(formArr);
		searchArticles(json);
		$("#form-status").html("");
		$("#search-results").html("");
	});
});

function formToJson(form){
	const json = {};
	const fields = form.values();
	for(const field of fields){
		json[field.name] = field.value || '';
	}
	return json;
}

function getLi(index, article){
	var $a = $("<a>", {
		"class": "list-group-item list-group-item-action align-items-center d-flex justify-content-between ",
		href: article.url,
		target: "_blanc",
		html: "<span class='rank'><b>" + index + ".</b> <span class='title'>" + article.title + "</span></span>"
	});
	var $badge = $("<span>", {
		"class": "badge badge-primary badge-pill",
		text: article.score
	});
	$a.append($badge);
	return $a;
}

function searchArticles(form){
	let url = `API/article/search/`;
	if(form){
		url += `?query=${form.query}&nocache=${new Date().getTime()}`;
	}
	$.getJSON(url, function(data) {
		$.each(data, function(index, article){
			$("#search-results").append(getLi(index+1, article));
		});
	});
}