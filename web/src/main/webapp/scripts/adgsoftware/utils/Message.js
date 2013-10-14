/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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
	"dojo/dom",
	"dojo/_base/fx",
	"dojo/fx",
	"dojo/on",
	"dojo/dom-style",
	"dojo/dom-construct"
], function(declare, dom, fx, fx2, on, style, domConstruct){
	var Message = declare(null, {
		displayError: function(msg){
			var n = domConstruct.create("h4", {class: "alert_error", innerHTML: msg}); //,"main", "first");
			style.set(n, "display", "none");
			style.set(n, "opacity", "0");
			domConstruct.place(n, "messagebox", "first");

			on(n, "click", function(){
				style.set(n, "opacity", "1");
				var animA = fx.fadeOut({node: n});
				var animB = fx2.wipeOut({node: n,
					onEnd: function() {domConstruct.destroy(n);}
				});
				fx2.chain([animA, animB]).play();  
			});

			var animA = fx.fadeIn({node: n});
			var animB = fx2.wipeIn({node: n});
			fx2.combine([animB, animA]).play();
		},

		displayInformation: function(msg) {
			var n = domConstruct.create("h4", {class: "alert_info", innerHTML: msg});
			style.set(n, "display", "none");
			style.set(n, "opacity", "0");
			domConstruct.place(n, "messagebox", "first");

			// TODO lunch not on click but after 5 sec
			on(n, "click", function(){
				style.set(n, "opacity", "1");
				var animA = fx.fadeOut({node: n});
				var animB = fx2.wipeOut({node: n,
					onEnd: function() {domConstruct.destroy(n);}
				});
				fx2.chain([animA, animB]).play();  
			});

			var animA = fx.fadeIn({node: n});
			var animB = fx2.wipeIn({node: n});
			fx2.combine([animB, animA]).play();
		},

		displayWarning: function(msg) {
			var n = domConstruct.create("h4", {class: "alert_warning", innerHTML: msg});
			style.set(n, "display", "none");
			style.set(n, "opacity", "0");
			domConstruct.place(n, "messagebox", "first");
			// TODO lunch not on click but after 5 sec
			on(n, "click", function(){
				style.set(n, "opacity", "1");
				var animA = fx.fadeOut({node: n});
				var animB = fx2.wipeOut({node: n,
					onEnd: function() {domConstruct.destroy(n);}
				});
				fx2.chain([animA, animB]).play();  
			});
			var animA = fx.fadeIn({node: n});
			var animB = fx2.wipeIn({node: n});
			fx2.combine([animB, animA]).play();
		},

		displaySuccess: function(msg) {
			var n = domConstruct.create("h4", {class: "alert_success", innerHTML: msg});
			style.set(n, "display", "none");
			style.set(n, "opacity", "0");
			domConstruct.place(n, "messagebox", "first");

			// TODO lunch not on click but after 5 sec
			on(n, "click", function(){
				style.set(n, "opacity", "1");
				var animA = fx.fadeOut({node: n});
				var animB = fx2.wipeOut({node: n,
					onEnd: function() {domConstruct.destroy(n);}
				});
				fx2.chain([animA, animB]).play();  
			});

			var animA = fx.fadeIn({node: n});
			var animB = fx2.wipeIn({node: n});
			fx2.combine([animB, animA]).play();
		}
	});

	return new Message();
});