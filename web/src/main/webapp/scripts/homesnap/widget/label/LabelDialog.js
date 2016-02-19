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
	"dijit/_WidgetBase",
	"dijit/_TemplatedMixin",
	"dijit/Dialog",
	"dijit/Tooltip",
	"dojo/text!./templates/LabelDialog.html"
], function(declare, _WidgetBase, _TemplatedMixin, Dialog, Tooltip, template) {
		return declare([Dialog, _WidgetBase, _TemplatedMixin], {
			content: template,
			title: 'Label',
			style: 'width: 300px',
			store: null,
			add: function(where, title) {
				var controller = {id: where};
				controller.where = where;
				controller.title = title;
				this.store.add(controller); // {id: where, parent: '1'} TODO ajoute tjs au group 10
				console.log(this.store);
			}
		});
});
