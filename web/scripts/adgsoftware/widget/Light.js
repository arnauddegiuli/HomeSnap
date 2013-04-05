define([
    "dojo/_base/declare",
    "dijit/form/ToggleButton",
    "dijit/form/HorizontalSlider",
    "dijit/_WidgetBase",
    "dijit/_TemplatedMixin",
    "dojo/text!./templates/Light.html",
    "dojo/dom-style",
    "dojo/_base/fx",
    "dojo/_base/lang"
], function(declare, ToggleButton, HorizontalSlider, _WidgetBase, _TemplatedMixin, template, domStyle, baseFx, lang) {
	
	return declare([_WidgetBase, _TemplatedMixin], {
		baseClass: "light",
		templateString: template,
		button: null,
		slider: null,
		lock: false,
		on : function() {
			this.set('label', 'ON');
			this.button.set('iconClass', 'lightOnIcon');
			if (!this.lock) {
				this.lock = true;
				this.slider.set('value', 0);
				this.lock = false
			}
		},
		off: function() {
			this.set('label', 'OFF');
			this.button.set('iconClass', 'lightOffIcon');
			if (!this.lock) {
				this.lock = true;
				this.slider.set('value', 100);
				this.lock = false
			}
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
			          } else {
			        	  component.off();
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