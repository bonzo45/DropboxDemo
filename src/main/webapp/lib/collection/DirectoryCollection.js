var DirectoryCollection = Backbone.Collection.extend({
	// URL to pull from server
	url : URL_DIRECTORY,

	model : FileModel
});