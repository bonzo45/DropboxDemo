var DirectoryView = Backbone.View.extend({	
  // HTML/CSS
  tagName: 'div',
  className: 'todoList',
  
  template: $("#directory-template").html(),

  // Model
  files: [],
  fileViews: [],
  
  // Take in all the files, create views for them all.
  initialize: function(directoryCollection) {
    this.collection = directoryCollection;
    this.listenTo(this.collection, 'change', this.render);
    this.listenTo(this.collection, 'reset', this.render);
  },

  events: {
    'click': 'render',
  },

  // Display the folder
  render: function() {
	var compiledTemplate = _.template(this.template);
	var contents = "";
	
	// For each of the files in the folder
    this.collection.models.forEach(function(fileModel) {
    	// Create a view for it
    	newView = new FileView(fileModel);
    	
    	// Append it
    	contents += newView.render().outerHTML;
    }, this);

    var data = {stuff: contents};
	var html = compiledTemplate(data);
	
    this.$el.html(html);
    return this.el;
  }
});