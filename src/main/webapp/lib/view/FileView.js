var FileView = Backbone.View.extend({
	// Put this view in the directory div
	tagName : "tr",

	// The template can be found here
	template: $("#todo-template").html(),

	initialize: function(filemodel) {
		this.model = filemodel;
		// When the model changes, re-render
		this.listenTo(this.model, 'change', this.render);
	},

	events : {
		'click' : 'render'
	},

	// Rendering simply applies the template
	render: function() {
		var compiledTemplate = _.template(this.template);
		var data = this.model.toJSON();
		var html = compiledTemplate(data);
		this.$el.html(html);
		return this.el;
	}
});