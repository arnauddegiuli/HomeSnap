define([
	"dojo/_base/declare",
	"dojo/request",
	"dojo/dom",
	"dojo/dom-construct",
	"dojo/json",
	"dojo/store/JsonRest",
	"dojo/store/Memory",
	"dojo/store/Cache",
	"adgsoftware/widget/label/Label",
	"dojo/store/Observable",
	"adgsoftware/utils/Message"
], function(declare, request, dom, domcConstruct,JSON, JsonRest, Memory, Cache, Label, Observable, msg) {

	var masterStore = new JsonRest({
		target: "/house/"
	});
	var cacheStore = new Memory({ });


	return declare(null, {
		baseClass: "house",
		houseStore: null,
		labels: null,
		group: null,
		load: function(url) {
		},
//		addLabel: function(label, title) {
//			
//		},	
		init: function() {
			this.houseStore = new Cache(masterStore, cacheStore);
			var oldPut = this.houseStore.put;
			this.houseStore.put = function(object, options){
				if(object.id == null){
					throw new Error("Id must be provided");
				}
				// now call the original
				oldPut.call(this, object, options);
			};
		},
		draw: function(container) {
			if (!container)
				container = dom.byId("house");
			results = this.houseStore.query();
			var labels = this.labels = new Array();
			// results object provides a forEach method for iteration
			results.forEach(function(label){
				domcConstruct.create("li", {innerHTML: "<a href='#/house/" + label.id + "'>" + label.title + "</a>", class: label.icon}, container);
				console.log("toto");
				console.log(label);
				console.log(dom.byId("main"));
				var div = domcConstruct.create("div", {id: "labelContainer_" + label.id}, "main");
				console.log(div);
				var widget = new Label({label: label}, div);
				labels.push(widget);
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