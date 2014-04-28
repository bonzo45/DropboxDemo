var DirectoryView = Backbone.View.extend({	
  // HTML/CSS
  tagName: 'div',
  className: 'todoList',
  
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
	this.$el.html("<tr><th></th><th><i class=\"fa fa-dropbox\"></i></th><th>Name</th><th>Size</th><th>Modified</th><th>Dropbox Path</th></tr>");
	// For each of the files in the folder
    this.collection.models.forEach(function(fileModel) {
    	// Create a view for it
    	newView = new FileView(fileModel);
    	
    	// Append it
    	this.$el.append(newView.render());
    }, this);

    return this.el;
  }
});