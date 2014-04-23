var DirectoryView = Backbone.View.extend({	
  // HTML/CSS
  tagName: 'div',
  className: 'todoList',

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
    this.$el.html("<tr><th>Name</th><th>Path</th><th>...</th>");
	  
	// For each of the files in the folder
    this.collection.models.forEach(function(fileModel) {
    	// Create a view for it
    	newView = new FileView(fileModel);
    	
    	// Append it
    	this.$el.append(newView.render());
    }, this);

    this.$el.html("<table>" + this.$el.html() + "</table>");
    return this.el;
  }
});