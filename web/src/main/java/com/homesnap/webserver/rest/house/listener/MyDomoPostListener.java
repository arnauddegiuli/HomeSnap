package com.homesnap.webserver.rest.house.listener;

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

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.light.Light;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.impl.OpenWebNetControllerService;
import com.homesnap.webserver.rest.house.MyDomoRestAPI;
import com.homesnap.webserver.rest.house.RestOperationException;
import com.homesnap.webserver.rest.house.UnsupportedRestOperation;
import com.homesnap.webserver.rest.house.Verb;
import com.homesnap.webserver.rest.house.utils.JSonTools;

// Creation
public class MyDomoPostListener extends MyDomoRestListenerAbstract 
implements MyDomoRestAPI {

	private ControllerService service = new OpenWebNetControllerService("localhost", 1234, 12345); // TODO use a factory?
	private String json;

	public MyDomoPostListener(House house, String uri, Map<String, String[]> parameters, String body) {
		super(house, uri, parameters);
		this.json = body;
	}

	@Override
	public void onHouse() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.POST);
	}

	@Override
	public void onLabelList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.POST);
	}

	@Override
	public void onLabel(String labelId) throws RestOperationException {
		Label l = getLabel(labelId);
		if (l!=null) {
			throw new RestOperationException(getUri(), Verb.POST, "Label [id:"+labelId+"] already exist.");
		}

		try {
			l = new Label();
			JSONObject j = JSonTools.fromJson(json);
			l.fromJson(j);
			l.setId(labelId);
		} catch (Error e) {
			throw new RestOperationException(getUri(), Verb.POST, "Label JSON representation is wrong ["+json+"].");
		}
		getHouse().getLabels().add(l);
		setResult(JSonTools.toJson(l));
	}

	@Override
	public void onControllerByLabel(String labelId, String where) throws RestOperationException {
		Controller c = getControllerByLabel(labelId, where);
		if (c!=null) {
			throw new RestOperationException(getUri(), Verb.POST, "Controller [id:"+where+"] already exist in this label.");
		}

		Label l = getLabel(labelId);
		if (l==null) {
			throw new RestOperationException(getUri(), Verb.POST, "Label [id:"+labelId+"] doesn't exist.");
		}

		c = getController(where);
		if(c == null) {
			throw new RestOperationException(getUri(), Verb.POST, "Controller [id:"+where+"] doesn't exist. A controller must have been created in a Group before being associated to a label.");
		} else {
			c.setWhere(new Where(where, where));
		}

		l.getControllerList().add(c);
		setResult(JSonTools.toJson(c));
	}

	@Override
	public void onGroupList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.POST);
	}

	@Override
	public void onGroup(String groupId) throws RestOperationException {
		Group g = getGroup(groupId);
		if (g!=null) {
			throw new RestOperationException(getUri(), Verb.POST, "Group [id:"+groupId+"] already exist.");
		}

		try {
			g = new Group();
			JSONObject j = JSonTools.fromJson(json);
			g.fromJson(j);
			g.setId(groupId);
		} catch (Error e) {
			throw new RestOperationException(getUri(), Verb.POST, "Group JSON representation is wrong ["+json+"].");
		}
		getHouse().getGroups().add(g);
		setResult(JSonTools.toJson(g));
	}

	@Override
	public void onControllerByGroup(String groupId, String where) throws RestOperationException {
		Controller c = getController(where);
		if (c!=null) {
			throw new RestOperationException(getUri(), Verb.POST, "Controller [id:"+where+"] already exist.");
		}

		Group g = getGroup(groupId);
		if (g==null) {
			throw new RestOperationException(getUri(), Verb.POST, "Group [Group:"+groupId+"] doesn't exist.");
		}

		c = createController(JSonTools.fromJson(json), where);
		g.getControllerList().add(c);
		setResult(JSonTools.toJson(c));
	}

	@Override
	public void onController(String where) throws RestOperationException, UnsupportedRestOperation {
		// Creation only in a physical group.
		throw new UnsupportedRestOperation(getUri(), Verb.POST);
	}

	private Controller createController(JSONObject json, String where) throws RestOperationException {
		Controller c = null;
		try {
			String who = json.getString(Controller.JSON_WHO);
			Class<? extends Controller> clazz; // TODO revoir la création
			switch (Who.valueOf(who)) {
			case LIGHT:
				clazz = Light.class;
				break;
			default:
				clazz = null;
				break;
			}
			c = service.createController(clazz, where);
			c.fromJson(json);
			c.setWhere(new Where(where, where));
		} catch (JSONException e) {
			throw new RestOperationException(getUri(), Verb.POST, "Controller JSON representation is wrong ["+json+"].");
		} catch (Error e) {
			throw new RestOperationException(getUri(), Verb.POST, "Unknown excetion with ["+json+"].");
		}
		return c;
	}

}
