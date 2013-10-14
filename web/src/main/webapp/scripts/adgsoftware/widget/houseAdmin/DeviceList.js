define([
	"dojo/_base/declare",
	"dojo/aspect",
	"dojo/store/Memory",
	"dojo/store/Observable",
	"dijit/Tree",
	"dijit/tree/ObjectStoreModel",
	"dijit/tree/dndSource",
	"adgsoftware/utils/Message"
], function(declare, aspect, Memory,Observable, Tree, ObjectStoreModel, dndSource, msg) {
	// Create test store, adding the getChildren() method required by ObjectStoreModel
	var myStore = new Memory({
		data: [{id:'house', title:'House', labels:[{"id":"ch1", "title":"Chambre 1", "icon":"icn_categories","controllers":[{"where":"12", "who":"1", "title":"toto"},{"where":"13", "who":"1", "title":"Light 2"},{"where":"14", "who":"1", "title":"Light 3"}]},{"id":"ch2", "title":"Chambre 2", "icon":"icn_categories","controllers":[{"where":"15", "who":"1", "title":"Light ch2"}]},{"id":"cui", "title":"Cuisine", "icon":"icn_categories","controllers":[{"where":"16", "who":"1", "title":"Light Cuisine"}]}],groups:[{"id":"1", "title":"Group 1","controllers":[{"where":"12", "who":"1", "title":"toto"},{"where":"13", "who":"1", "title":"Light 2"},{"where":"14", "who":"1", "title":"Light 3"},{"where":"15", "who":"1", "title":"Light ch2"},{"where":"16", "who":"1", "title":"Light Cuisine"}]}]}],
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
