/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
define([
	"dojo/_base/declare",
	"dojo/aspect",
	"dojo/store/Observable",
	"dijit/Tree",
	"dijit/tree/ObjectStoreModel",
	"dijit/tree/dndSource",
	"homesnap/utils/Message"
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
