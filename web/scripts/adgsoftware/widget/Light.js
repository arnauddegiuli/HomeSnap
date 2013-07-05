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
    "dojo/json"
], function(declare, ToggleButton, HorizontalSlider, _WidgetBase, _TemplatedMixin, template, domStyle, baseFx, lang, request, JSON) {
	
	return declare([_WidgetBase, _TemplatedMixin], {
		baseClass: "light",
		templateString: template,
		button: null,
		slider: null,
		lock: false,
		adress: "12",
		switchOnOff : function(status, value) {
			var component = this;
			if (!component.lock) {
				component.lock = true;
				// Call REST API to change light
				request.put("/light/" + this.adress + "/" + status, {sync: true, handleAs: "json"})
					.then(function(data){
						if (data.status == status) { 
							if ("on" == status) {
								component.set('label', 'ON');
								component.button.set('iconClass', 'lightOnIcon');
								component.slider.set('value', value);
							} else { // Off
								component.set('label', 'OFF');
					        	component.button.set('iconClass', 'lightOffIcon');
					        	component.slider.set('value', value);
							}
							
						} else {
							console.log('Error from server');
						}
	    		    }, function(error) {
	    		    	alert(error);
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
			        	  component.switchOnOff("on", 100);
			        	  console.log('on');
			          } else {
			        	  component.switchOnOff("off", 0);
			        	  console.log('off');
			          }
				},
			  	postCreate: function() {
			    	this.set('showLabel', false);
			    }
				 
			 }, this.id + "progButtonNode");
		 

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
	});
		
});