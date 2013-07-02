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
		on : function() {
				adress = "12"; // TODO adress hard coded...
				// Call REST API to change light
				var component = this;
				request.put("/light/" + adress + "/on", {sync: true, handleAs: "json"})
					.then(function(data){
						if (data.status == "on") {
							component.set('label', 'ON');
							component.button.set('iconClass', 'lightOnIcon');
							if (!component.lock) {
								component.lock = true;
								component.slider.set('value', 0);
								component.lock = false
							}
						} else {
							console.log('beurk!');
						}
	    		    }, function(error) {
	    		    	alert(error);
	    	   });
		},
		off: function() {
			adress = "12"; // TODO adress hard coded...
			var component = this;
	    	 // Call REST API to change light
	    	 request.put("/light/" + adress + "/off", {sync: true, handleAs: "json"})
			    .then(function(data){
		        	  if (data.status == "off") {
			        	  component.set('label', 'OFF');
			        	  component.button.set('iconClass', 'lightOffIcon');
			  			if (!component.lock) {
			  				component.lock = true;
			  				component.slider.set('value', 100);
			  				component.lock = false
			  			}
			          } else {
			        	  console.log('beurk!');
			          }
	    		    }, function(error) {
	    		    	alert(error);
	    	   });
		},
		startup: function() {
			var component = this;
			this.button = new ToggleButton ({
				checked: false,
			  	iconClass: 'lightOffIcon',
			  	label: 'OFF',
			  	onChange: function(val){
			    	if (val) {
			        	  component.on();
			        	  console.log('on');
			          } else {
			        	  component.off();
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
		        		 component.lock = true;
			        	 if (value > 0) {
			            	 component.button.onChange(true);
			             } else {
			            	 component.button.onChange(false);
			             }
			        	 component.lock = false
		 			}
		         }
		     }, this.id+"slider");
		}
	});
		
});