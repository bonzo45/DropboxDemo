function dropboxInit() {
	setupChooser();
}

function redirectToDropbox() {
	$("#redirect_form").attr("action", URL_REDIRECT_TO_DROPBOX);
	$("#redirect_form")[0].submit();
}

function getAccountDetails() {
	var access_token = $("#access_token").val();
	$.getJSON("dropbox/account_details", {
		"access_token" : access_token
	}, function(data) {
		var account_details_div = $("#account_details");
		account_details_div.html(data.id + " - " + data.name + " ("
				+ data.country + ")");
	});

	return false;
}

function setupChooser() {
	/* Insert Dropbox 'chooser' (for pulling files from Dropbox) */
	options = {
		success : function(files) {
			var regex = /https:\/\/dl.dropboxusercontent.com\/1\/view\/([^\/]*)\/(.*)/;
			var matches = regex.exec(files[0].link);
			var path = matches[2];
			var accessToken = $("#access_token").val();
			var urlToContact = URL_FILE + path
					+ URL_FILE_FROM_DROPBOX_SUFFIX + "?access_token=" + accessToken;
			$.ajax({
				type : "PUT",
				url : urlToContact,
				complete : function() {
					rootDirectory.fetch({
						reset : true
					});
				}
			});
		},

		linkType : "direct",

		// TODO: Allow many files to be pulled at once.
		multiselect : false
	};
	var button = Dropbox.createChooseButton(options);
	document.getElementById("dropbox_chooser").appendChild(button);
}