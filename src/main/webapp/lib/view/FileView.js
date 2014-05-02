var FileView = Backbone.View.extend({
	// Put this view in the directory div
	tagName : "tr",

	// Template can be found here
	template: $("#file-template").html(),

	initialize: function(filemodel) {
		this.model = filemodel;
		// When the model changes, re-render
		this.listenTo(this.model, 'change', this.render);
	},

	events : {
		'click' : 'uploadToDropbox'
	},

	// Rendering simply applies the template
	render: function() {
		var compiledTemplate = _.template(this.template);
		var data = this.model.toJSON();
		var html = compiledTemplate(data);
		this.$el.html(html);
		return this.el;
	},
	
	uploadToDropbox: function() {
		var accessToken = $("#access_token").val();
		var urlToContact = URL_FILE + this.model.get('path') + this.model.get('name') + URL_FILE_TO_DROPBOX_SUFFIX + "?access_token=" + accessToken;
		$.ajax({
			type: "PUT",
			url: urlToContact,
			complete: function() {
				rootDirectory.fetch({reset: true});
			}
		});
	}
});