 var FileModel = Backbone.Model.extend({
	// URL used to fetch models of this type from the server.
	urlRoot: URL_FILE,
	
	// Default Values
	defaults: {
		name: "",
		path: "",
		creationTime: 0,
		lastAccessTime: 0,
		lastModifiedTime: 0,
		isDirectory: false,
		isOther: false,
		isRegularFile: false,
		isSymbolicLink: false,
		size: 0,
		isInDropbox: false,
		dropboxPath: ""
	}
});