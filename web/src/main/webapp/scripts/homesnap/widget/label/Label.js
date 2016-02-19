/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
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
	"homesnap/widget/light/Light",
	"homesnap/utils/Message"
], function(declare, _WidgetBase, _TemplatedMixin, template, dom, domConstruct, domStyle, baseFx, lang, request, JSON, Light, msg) {
	return declare([_WidgetBase, _TemplatedMixin], {
		base: "label",
		templateString: template,
		controllers: new Array(),
		label: null,
		house: null,
		constructor: function(args) {
			declare.safeMixin(this,args);

			this.label = args.label;
			this.house = args.house;
			var index;
			for (index = 0; index < this.label.controllers.length; ++index) {
				var where = this.label.controllers[index].where;
				var controllerId = "deviceContainer_" + where;
				// Create the container for the device
				domConstruct.create("div", {id: controllerId}, dom.byId("labelContainer_" +this.label.id));
				// Create the device
				switch(this.label.controllers[index].who) {
					case "LIGHT":
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
