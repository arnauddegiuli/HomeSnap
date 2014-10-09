package com.homesnap.webserver.rest.listener;

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

import java.util.Map;

import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.Verb;
import com.homesnap.webserver.utils.JSonTools;


// Modification
public class MyDomoPutListener extends MyDomoRestListenerAbstract implements MyDomoRestAPI {

//	private ControllerService service = new OpenWebNetControllerService("localhost", 1234, 12345);
//	private Map<String, Light> lightList = new Hashtable<String, Light>();

	private String json;

	public MyDomoPutListener(House house, String uri, Map<String, String[]> parameters, String body) {
		super(house, uri, parameters);
		this.json = body;
	}

	@Override
	public void onHouse() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onLabelList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onLabel(String labelId) throws RestOperationException {
		Label l = getLabel(labelId);
		if (l != null) {
			try {
				JSONObject j = JSonTools.fromJson(json);
				l.fromJson(j);
				setResult(JSonTools.toJson(l));
			} catch (Error e) {
				throw new RestOperationException(getUri(), Verb.PUT, "Label JSON representation is wrong ["+json+"].");
			}
		} else {
			throw new RestOperationException(getUri(), Verb.PUT, "Label [id:"+labelId+"] not found.");
		}
	}

	@Override
	public void onControllerByLabel(String labelId, String where) throws RestOperationException {
		Controller c = getControllerByLabel(labelId, where);
		updateController(c, "Controller [id:"+where+"] not found in label [" + labelId + "].");
	}

	@Override
	public void onGroupList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onGroup(String groupId) throws RestOperationException {
		Group g = getGroup(groupId);
		if (g != null) {
			try {
				JSONObject j = JSonTools.fromJson(json);
				g.fromJson(j);
				setResult(JSonTools.toJson(g));
			} catch (Error e) {
				throw new RestOperationException(getUri(), Verb.PUT, "Group JSON representation is wrong ["+json+"].");
			}	
		} else {
			throw new RestOperationException(getUri(), Verb.PUT, "Label [id:"+groupId+"] not found.");
		}
	}

	@Override
	public void onControllerByGroup(String groupId, String where) throws RestOperationException {
		Controller c = getControllerByGroup(groupId, where);
		updateController(c, "Controller [id:"+where+"] not found in group [" + groupId + "].");
	}

	@Override
	public void onController(String where) throws RestOperationException {
		Controller c = getController(where);
		updateController(c, "Controller [id:"+where+"] not found.");
		
	}

	private void updateController(Controller c, String errorMessage) throws RestOperationException {
		if (c != null) {
			try {
				JSONObject j = JSonTools.fromJson(json);
				c.fromJson(j);
				
				for (String name : getParameters().keySet()) {
					if (!"id".equalsIgnoreCase(name)) { // "id" is a special case manage by parser.
						String[] params = getParameters().get(name);
						if (params.length == 0) {
							System.err.println("Parameter [" + name + "] has no value... Nothing done.");
						} else {
							String param = params[0];
							if (params.length > 1) {
								System.err.println("Parameter [" + name + "] has more than one value... First one [" + param + "] used.");	
							}
							c.set(name, param);
						}
					}
				}
				
				setResult(JSonTools.toJson(c));
			} catch (Error e) {
				throw new RestOperationException(getUri(), Verb.PUT, "Controller JSON representation is wrong ["+json+"].");
			}	
		} else {
			throw new RestOperationException(getUri(), Verb.PUT, errorMessage);
		}
	}
}
