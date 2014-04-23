var DirectoryCollection = Backbone.Collection.extend({
	// URL to pull from server
	url : 'dropbox/directory/',

	model : FileModel
});