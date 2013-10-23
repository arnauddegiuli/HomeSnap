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
		});
});