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
	"dojo/request",
	"dojo/dom",
	"dojo/dom-construct",
	"dojo/json",
	"dojo/store/Cache",
	"homesnap/widget/label/Label",
	"dojo/store/Observable",
	"homesnap/utils/Message",
	"dojo/when"
], function(declare, request, dom, domcConstruct,JSON, Cache, Label, Observable, msg, when) {
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
			
			var labels = this.labels = new Array();
			var store = this.store;
			
			var value = this.store.query(); 
			// results object provides a forEach method for iteration
			value.results.forEach(function (house) {
				alert(house);
					dojo.forEach(house.labels, function(label) {
						domcConstruct.create("li", {innerHTML: "<a href='#/house/labels/" + label.id + "'>" + label.title + "</a>", class: label.icon}, container);
						var div = domcConstruct.create("div", {id: "labelContainer_" + label.id}, "main");
						var widget = new Label({label: label, house: store}, div);
						
						labels.push(widget);
					}, this);
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
