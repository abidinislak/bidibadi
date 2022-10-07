function clearfilter(){
	
	
	window.location=moduleURL;
}



function showDeleteconfirmModal(link,entityName){
	
	entityId= link.attr("entityId");
	
	$("#yesButton").attr("href",link.attr("href"));
	$("#confirmText").text("ar you sure want to delete this "+entityName+" ıd "+entityId + "?");
	$("#confirmModal").modal();
	
	
}