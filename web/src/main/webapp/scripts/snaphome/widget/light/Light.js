/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
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
	"dijit/form/ToggleButton",
	"dijit/form/HorizontalSlider",
	"dijit/_WidgetBase",
	"dijit/_TemplatedMixin",
	"dojo/text!./templates/Light.html",
	"dojo/dom-style",
	"dojo/_base/fx",
	"dojo/_base/lang",
	"dojo/request",
	"dojo/json",
	"snaphome/utils/Message"
], function(declare, ToggleButton, HorizontalSlider, _WidgetBase, _TemplatedMixin, template, domStyle, baseFx, lang, request, JSON, msg) {
	
	return declare([_WidgetBase, _TemplatedMixin], {
		baseClass: "light",
		templateString: template,
		button: null,
		slider: null,
		lock: false,
		adress: null,
		showSlider: false,
		switchOnOff : function(status, value) {
			var component = this;
			if (!component.lock) {
				component.lock = true;
				// Call REST API to change light
				request.put("/house/controllers/" + this.adress + "/" + status, {sync: true, handleAs: "json"})
					.then(function(data){
						if (data.status == status) { 
							if ("LIGHT_ON" == status) {
								component.set('label', 'ON');
								component.button.set('iconClass', 'lightOnIcon');
								component.slider.set('value', value);
							} else { // Off
								component.set('label', 'OFF');
								component.button.set('iconClass', 'lightOffIcon');
								component.slider.set('value', value);
							}
						} else {
							msg.displayError("Error from gateway. Status doesn't change. Try later.");
							// TODO raise an error to reset the button status to the previous value
						}
					}, function(error) {
						msg.displayError(error);
				});
				component.lock = false
			}
		},
		startup: function() {
			var component = this;
			this.button = new ToggleButton ({
				checked: false,
				iconClass: 'lightOffIcon',
				label: 'OFF',
				onChange: function(status, value){
					if (!value){
						status ? value = 100 : value = 0;
					}
					if (status) {
						component.switchOnOff("LIGHT_ON", 100);
						console.log('on');
					} else {
						component.switchOnOff("LIGHT_OFF", 0);
						console.log('off');
					}
				},
				postCreate: function() {
					this.set('showLabel', false);
				}
			}, this.id + "progButtonNode");

			if (this.showSlider) {
				this.slider = new HorizontalSlider({
					value: 0,
					minimum: 0,
					maximum: 100,
					intermediateChanges: true,
					style: "width: 300px",
					onChange: function(value){
						if (!component.lock) {
							if (value > 0) {
								component.button.onChange(true, value);
							} else {
								component.button.onChange(false, value);
							}
						}
					}
				}, this.id+"slider");
			}
		}
	});
});