define([
	"dojo/_base/declare",
	"dijit/_WidgetBase",
	"dijit/_TemplatedMixin",
	"dojo/text!./templates/Label.html",
	"dojo/dom",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/_base/fx",
	"dojo/_base/lang",
	"dojo/request",
	"dojo/json",
	"adgsoftware/widget/light/Light",
	"adgsoftware/utils/Message"
], function(declare, _WidgetBase, _TemplatedMixin, template, dom, domConstruct, domStyle, baseFx, lang, request, JSON, Light, msg) {
	return declare([_WidgetBase, _TemplatedMixin], {
		base: "label",
		templateString: template,
		controllers: new Array(),
		label: null,
		constructor: function(args) {
			declare.safeMixin(this,args);

			this.label = args.label;
			var index;
			for (index = 0; index < this.label.controllers.length; ++index) {
				var where = this.label.controllers[index].where;
				var controllerId = "deviceContainer_" + where;
				// Create the container for the device
				domConstruct.create("div", {id: controllerId}, dom.byId("labelContainer_" +this.label.id));
				// Create the device
				switch(this.label.controllers[index].who) {
					case "1":
						var l = new Light({adress: where}, controllerId);
						this.controllers.push(l);
						l.startup();
						break;

						default:
							console.log("Default controller!");
				}
			}
		}
	});
});