define([
    "dojo/_base/declare",
    "dojo/request",
    "dojo/dom",
    "dojo/dom-construct",
    "dojo/json",
    "dojo/store/JsonRest",
    "dojo/store/Memory",
    "dojo/store/Cache",
    "dojo/store/Observable",
    "adgsoftware/utils/Message"
], function(declare, request, dom, domcConstruct,JSON, JsonRest, Memory, Cache, Observable, msg) {
	
//	Label label = declare(, {baseClass:"label"});
//	Group group = declare(, {baseClass:"group"});
	
    var masterStore = new JsonRest({
        target: "/house/"
    });
    var cacheStore = new Memory({ });
	    

	
	
	return declare(null, {
		baseClass: "house",
		houseStore: null,
		label: null,
		group: null,
		load: function(url) {
			
		},
//		addLabel: function(label, title) {
//			
//		},	
		init: function() {
			houseStore = new Cache(masterStore, cacheStore);
						
			var oldPut = houseStore.put;
			houseStore.put = function(object, options){
			    if(object.id == null){
			        throw new Error("Id must be provided");
			    }
			    // now call the original
			    oldPut.call(this, object, options);
			};
			
			this.refresh();
		},
		refresh: function() {
			results = houseStore.query();
			var container = dom.byId("house");
			 
		    // results object provides a forEach method for iteration
		    results.forEach(function(label){
		    	domcConstruct.create("li", {innerHTML: "<a href='#'>" + label.title + "</a>"}, container);
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