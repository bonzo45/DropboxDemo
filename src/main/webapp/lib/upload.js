function triggerUpload() {	
	// Trigger the file picker.
	$("#upload_file_field").click();
}

// Asynchronous File Upload
function uploadFile() {
	var fileObj = $("#upload_file_field");
	var filePath = fileObj.val();
	var fileName = filePath.split("\\").pop();
	var formURL = URL_FILE + fileName;
	
	/* Build FormData object using the file field */
	var formData = new FormData();
	formData.append('file', fileObj[0].files[0]);
	
	/* Asynchronously POST the file */
	$.ajax({
		url : formURL,
		type : 'POST',
		data : formData,
		mimeType : "multipart/form-data",
		contentType : false,
		cache : false,
		processData : false,
		success : function(data, textStatus, jqXHR) {
			rootDirectory.fetch({
				reset : true
			});
		},
		error : function(jqXHR, textStatus, errorThrown) {

		}
	});
	
	/* Override the default action of the form */
	return false;
}