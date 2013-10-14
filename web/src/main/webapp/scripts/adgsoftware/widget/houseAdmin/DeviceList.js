define([
	"dojo/_base/declare",
	"dojo/aspect",
	"dojo/store/Observable",
	"dijit/Tree",
	"dijit/tree/ObjectStoreModel",
	"dijit/tree/dndSource",
	"adgsoftware/store/HouseStore",
	"adgsoftware/utils/Message"
], function(declare, aspect, Observable, Tree, ObjectStoreModel, dndSource, HouseStore, msg) {
	// Create test store, adding the getChildren() method required by ObjectStoreModel
	var myStore = new HouseStore({
		getChildren: function(object){
			if (object.id=='house')
				return object.groups;
			else
				return object.controllers;
		}
	});

	aspect.around(myStore, "put", function(originalPut){
		// To support DnD, the store must support put(child, {parent: parent}).
		// Since memory store doesn't, we hack it.
		// Since our store is relational, that just amounts to setting child.parent
		// to the parent's id.
		return function(obj, options){
			if(options && options.parent){
				obj.parent = options.parent.id;
			}
			return originalPut.call(myStore, obj, options);
		}
	});

	 myStore = new Observable(myStore);
	 
	// Create the model
	var myModel = new ObjectStoreModel({
		store: myStore,
		query: {id: 'house'}
	});

	return declare([Tree], {
		showRoot: false,
		model: myModel,
		dndController: dndSource,
		getLabel: function(item) {
			 return item.title;
		},
		getIconClass: function(/*dojo.store.Item*/ item, /*Boolean*/ opened){
			// TODO manage different device icon
			return (!item || item.controllers) ? (opened ? "dijitFolderOpened" : "dijitFolderClosed") : "dijitLeaf";
		}
	});
	
});
