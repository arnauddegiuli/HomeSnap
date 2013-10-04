define([
    "dojo/_base/declare",
    "adgsoftware/widget/Light",
    "adgsoftware/utils/Message"
], function(declare, Light, msg) {
	// TODO gérer soit le label fourni soit un label avec id et par REST on récupére les infos
	return declare(null, {
		controllers: new Array(),
		label: null,
		constructor: function(label) {
			declare.safeMixin(this,label);
			this.label = label;
			
			var index;
			for (index = 0; index < label.controllers.length; ++index) {
			    console.log(label.controllers[index]);
			    switch(label.controllers[index].who) {
			       case "1":
			    	   var l = new Light({}, "lightContainer" + label.controllers[index].where);
			    	   this.controllers.push(l);
			    	   l.startup();
			    	   console.log("Light controller" + l);
			    	   break;
			    	   
			    	   default:
			    		   console.log("Default controller!");
			    }
			}
			console.log("Label construction done!");
		}
	
	
	});
});