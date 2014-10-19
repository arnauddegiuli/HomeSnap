/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
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
	"dojo/text!./templates/OpenWebNetConsole.html",
	"dojo/request",
	"dojo/json",
	"dojo/dom",
	"dojo/dom-construct",
	"homesnap/utils/OpenWebNetMessage",
	"homesnap/utils/Message"
], function(declare, _WidgetBase, _TemplatedMixin, template, request, JSON, dom, domConstruct, openWebNetMsg, msg) {
	
	return declare([_WidgetBase, _TemplatedMixin], {
		baseClass: "openWebNetConsole",
		templateString: template,
		_onFocus: function(event) {
			console.log(event);
			if(!event.target._haschanged){event.target.value=''};event.target._haschanged=true;
		},
		
		_onClick: function() {
			var div = domConstruct.create("div", {class:"message"}, dom.byId("messageList_" +this.id));
			var message = "SENT: [" + dom.byId("message_" +this.id).value + "]<br/>RECEIVED: [*#*1##]"; 
			domConstruct.create("p", {innerHTML:message}, div);
		}
	});
});
