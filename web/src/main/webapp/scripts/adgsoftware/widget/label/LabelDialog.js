define([
	"dojo/_base/declare",
	"dijit/_WidgetBase",
	"dijit/_TemplatedMixin",
	"dijit/Dialog",
	"dijit/Tooltip",
	"dojo/text!./templates/LabelDialog.html"
], function(declare, _WidgetBase, _TemplatedMixin, Dialog, Tooltip, template) {
		return declare([Dialog, _WidgetBase, _TemplatedMixin], {
			content: template,
			title: 'Label',
			style: 'width: 300px',
			store: null,
			add: function(where, title) {
				var controller = {id: where};
				controller.where = where;
				controller.title = title;
				this.store.add(controller); // {id: where, parent: '1'} TODO ajoute tjs au group 10
				console.log(this.store);
			}
		});
});