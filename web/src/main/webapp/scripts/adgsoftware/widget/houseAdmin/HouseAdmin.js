define([
	"dojo/_base/declare",
	"dojo/aspect",
	"dojo/store/Observable",
	"dijit/Tree",
	"dijit/tree/ObjectStoreModel",
	"dijit/tree/dndSource",
	"adgsoftware/utils/Message"
], function(declare, aspect, Observable, Tree, ObjectStoreModel, dndSource, msg) {
	
	return declare([Tree], {
		showRoot: false,
		model: null,
		dndController: dndSource,
		getLabel: function(item) {
			 return item.title;
		},
		getIconClass: function(/*dojo.store.Item*/ item, /*Boolean*/ opened){
			// TODO manage different device icon
			return (!item || item.controllers) ? (opened ? "dijitFolderOpened" : "dijitFolderClosed") : "dijitLeaf";
		},
		constructor: function(args) {
			args.model = new ObjectStoreModel({
				store: args.store,
				query: {id: 'house'},
				mayHaveChildren: function (item) {
					return item.controllers;
				}
			});
			declare.safeMixin(this,args);

			aspect.around(this.model.store, "getChildren", function(originalGetChildren){
				return function(obj, options){
					if (obj.id=='house')
						return obj.labels;
					else
						return obj.controllers;
				}
			});

//			aspect.around(this.model.store, "put", function(originalPut){
//				// To support DnD, the store must support put(child, {parent: parent}).
//				// Since memory store doesn't, we hack it.
//				// Since our store is relational, that just amounts to setting child.parent
//				// to the parent's id.
//				return function(obj, options){
//					if(options && options.parent){
//						obj.parent = options.parent.id;
//					}
//					return originalPut.call(this.model.store, obj, options);
//				}
//			});
			
			this.model.store = new Observable(this.model.store);
		}
	});
	
});
