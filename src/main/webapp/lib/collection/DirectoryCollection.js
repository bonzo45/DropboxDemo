var DirectoryCollection = Backbone.Collection.extend({
	// URL to pull from server
	url : DIRECTORY,

	model : FileModel
});