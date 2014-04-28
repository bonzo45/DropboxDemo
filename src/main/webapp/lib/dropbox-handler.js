function dropboxInit() {
	getAuthorisationLink();
	setupChooser();
}

function getAuthorisationLink() {
	$.getJSON("dropbox/auth/auth_link", function(data) {
		$("#authorisation_link").html(
				"<a href=\"" + data.auth_link + "\" target=\"_blank\">"
						+ data.auth_link + "</a>");
	});
}

function getAccessToken() {
	var authorisation_code = $("#authorisation_code").val();
	$.getJSON("dropbox/auth/access_token/" + authorisation_code,
			function(data) {
				$("#access_token_instructions").html(
						"Here is your access token, keep it safe!");
				$("#access_token_instructions")
						.addClass("accessTokenGenerated");
				$("#access_token").val(data.access_token);
			});
	// Stop form from being submitted normally.
	return false;
}

function getAccountDetails() {
	var access_token = $("#access_token").val();
	$.getJSON("dropbox/account_details", {
		"access_token" : access_token
	}, function(data) {
		var account_details_div = $("#account_details");
		account_details_div.html(data.user_id + " - " + data.name + " ("
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
			var urlToContact = "dropbox/files/" + path
					+ "/from_dropbox" + "?access_token=" + accessToken;
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