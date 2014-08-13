define([
	"dojo/_base/declare",
	"dojo/request",
	"dojo/dom",
	"dojo/dom-construct",
	"dojo/json",
	"dojo/store/Cache",
	"homesnap/widget/label/Label",
	"dojo/store/Observable",
	"homesnap/utils/Message"
], function(declare, request, dom, domcConstruct,JSON, Cache, Label, Observable, msg) {
	return declare(null, {
		baseClass: "house",
		store: null,
		labels: null,
		group: null,
		load: function(url) {
		},
//		addLabel: function(label, title) {
//			
//		},	
		draw: function(container) {
			if (!container)
				container = dom.byId("house");
			var results = this.store.query();
			var labels = this.labels = new Array();
			var store = this.store;
			// results object provides a forEach method for iteration
			results.results.forEach(function(house){
				dojo.forEach(house.labels, function(label) {
					domcConstruct.create("li", {innerHTML: "<a href='#/house/labels/" + label.id + "'>" + label.title + "</a>", class: label.icon}, container);
					var div = domcConstruct.create("div", {id: "labelContainer_" + label.id}, "main");
					var widget = new Label({label: label, house: store}, div);
					labels.push(widget);
				});
			} /*insertRow*/);

//		    results.observe(function(item, removedIndex, insertedIndex){
//		        // this will be called any time a item is added, removed, and updated
//		        if(removedIndex > -1){
//		            removeRow(removedIndex);
//		        }
//		        if(insertedIndex > -1){
//		            insertRow(item, insertedIndex);
//		        }
//		    }, true); // we can indicate to be notified of object updates as well
		},
		constructor: function(args) {
			declare.safeMixin(this,args);
		}
//		 
//		    function insertRow(item, i){
//		        var row = domConstruct.create("div", {
//		            innerHTML: item.name + " quantity: " + item.quantity
//		        });
//		        rows.splice(i, 0, container.insertBefore(row, rows[i] || null));
//		    }
//		 
//		    function removeRow(i){
//		        domConstruct.destroy(rows.splice(i, 1)[0]);
//		    }
//		}
	});
});