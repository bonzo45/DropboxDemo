<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="/DropboxDemo/styles.css">
  
  <!-- Backbone Underscore and JQuery -->
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
  <script src="/DropboxDemo/lib/underscore-min.js"></script>
  <script src="/DropboxDemo/lib/backbone-min.js"></script>
  
  <!-- Dropbox Chooser -->
  <script type="text/javascript" src="https://www.dropbox.com/static/api/2/dropins.js" id="dropboxjs" data-app-key="ar4df9h55qh7t6j"></script>
  
  <!-- Font Awesome - used for the file/folder and Dropbox icons -->
  <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

  <!-- Template for displaying a file -->
  <script type='text/template' id='file-template'>
      <td>
        <% 
        if (directory) {
            print('<i class="fa fa-folder"></i>')
        }
        else if (file) {
           print('<i class="fa fa-file"></i>')
        }        
        else {
            print('<i class="fa fa-question-circle"></i>')
        } 
        %>
      </td>

      <td>
        <% 
        if (inDropbox) {
          print('<i class="fa fa-dropbox yes"></i>');
        }
        else {
          print('<i class="fa fa-dropbox no"></i> ');
        }
        %>
      </td>

      <td>
        <%= name %>
      </td>

      <td>
        <%= size %>
      </td>

      <td>
        <%
          var date = new Date(parseInt(lastModifiedTime));
          print(date.getDate())
          print("/")
          print(date.getMonth() + 1)
          print("/")
          print(date.getFullYear())
        %>
      </td>

      <td>
        <%= dropboxPath %>
      </td>
  </script>
  
  <!-- URL Mappings -->
  <script src="/DropboxDemo/lib/url.js"></script>
  
  <!-- Backbone Model/View/Collections -->
  <script src="/DropboxDemo/lib/model/FileModel.js"></script>
  <script src="/DropboxDemo/lib/collection/DirectoryCollection.js"></script>
  <script src="/DropboxDemo/lib/view/FileView.js"></script>
  <script src="/DropboxDemo/lib/view/DirectoryView.js"></script>
  
  <!-- Pulls the application's authentication link and access code -->
  <script src="/DropboxDemo/lib/dropbox-handler.js"></script>
  
  <!-- File Uploading -->
  <script src="/DropboxDemo/lib/upload.js"></script>
  
	<script>
		function init() {
			dropboxInit();
	
			/* Display files at server 'root' directory */
			rootDirectory = new DirectoryCollection();
			rootDirectoryView = new DirectoryView(rootDirectory);
			rootDirectoryView.setElement("#directory");
			rootDirectory.fetch({
				reset : true
			});
		}
	</script>

<title>Sam's Demo</title>
</head>
<body onload="init()">
  
  <h1>Sam's Incredible Demonstration</h1>
  
  <form id="redirect_form" onSubmit="return redirectToDropbox()">
    <button type="submit" name="done" class="redirect_button">Click to Authenticate</button>
  </form>
  
  <div id="access_token_instructions" class="instructions">Been here before? Paste your access token here.</div>
  <form id="access_code_authentication" onSubmit="return getAccountDetails()">
    <input type="text" name="access_code" id="access_token" class="input" value="${model! ''}"></input>
    <input type="submit" name="done" class="submit"></input>
  </form>
  
  <div class="instructions">Your Dropbox Account</div>
  <div id="account_details">
  </div>
  
  <div class="instructions">Files on Server</div>
  <table id="directory">
  </table>

  <div class="instructions">Upload a File</div>
  <form id="upload_file" onSubmit="return uploadFile()" enctype="multipart/form-data">
    <input id="upload_file_field" type="file" name="file"></input>
    <input type="submit" name="done">
  </form>

  <div class="instructions">Pull from Dropbox</div>
  <div id="dropbox_chooser"></div>
  
</body>
</html>