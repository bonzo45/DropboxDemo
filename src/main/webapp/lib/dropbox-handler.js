function getAuthorisationLink() {
  $.getJSON("dropbox/auth_link", function(data) {
    $("#authorisation_link").html(
        "<a href=\"" + data.auth_link + "\" target=\"_blank\">"
            + data.auth_link + "</a>");
  });
}

function getAccessToken() {
  var authorisation_code = $("#authorisation_code").val();
  $.getJSON("dropbox/access_token", {
    "authorisation_code" : authorisation_code
  }, function(data) {
    $("#access_token_instructions").html(
        "Here is your access token, keep it safe!");
    $("#access_token_instructions").addClass("accessTokenGenerated");
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
    account_details_div.html(data.user_id + " - " + data.name + "(" + data.country + ")");
  });
  
  $.getJSON("dropbox/directory", {
    "access_token": access_token,
    "directory": "/"
  }, function(data) {
	  var directory_list = $("#directory");
	  data.children.forEach(function(child) {
		  directory_list.append("<li>" + child.icon_name + " - "+ child.name + " (" + child.path + ")" + "</li>");
	  });
  });
  
  return false;
}