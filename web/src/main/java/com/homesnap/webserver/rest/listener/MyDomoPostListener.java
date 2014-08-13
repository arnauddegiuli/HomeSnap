package com.homesnap.webserver.rest.listener;

import java.util.Map;

import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.impl.OpenWebNetControllerService;
import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.Verb;
import com.homesnap.webserver.utils.JSonTools;

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
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onLabelList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onLabel(String labelId) throws RestOperationException {
		Label l = getLabel(labelId);
		if (l!=null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Label [id:"+labelId+"] already exist.");
		}

		try {
			l = new Label();
			JSONObject j = JSonTools.fromJson(json);
			l.fromJson(j);
			l.setId(labelId);
		} catch (Error e) {
			throw new RestOperationException(getUri(), Verb.PUT, "Label JSON representation is wrong ["+json+"].");
		}
		getHouse().getLabels().add(l);
	}

	@Override
	public void onControllerByLabel(String labelId, String where) throws RestOperationException {
		Controller c = getControllerByLabel(labelId, where);
		if (c!=null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Controller [id:"+where+"] already exist in this label.");
		}

		Label l = getLabel(labelId);
		if (l==null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Label [id:"+labelId+"] doesn't exist.");
		}

		c = getController(where);
		if(c == null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Controller [id:"+where+"] doesn't exist. A controller must have been created in a Group before being associated to a label.");
		} else {
			c.setWhere(new Where(where, where));
		}

		l.getControllerList().add(c);
	}

	@Override
	public void onGroupList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onGroup(String groupId) throws RestOperationException {
		Group g = getGroup(groupId);
		if (g!=null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Group [id:"+groupId+"] already exist.");
		}

		try {
			g = new Group();
			JSONObject j = JSonTools.fromJson(json);
			g.fromJson(j);
			g.setId(groupId);
		} catch (Error e) {
			throw new RestOperationException(getUri(), Verb.PUT, "Group JSON representation is wrong ["+json+"].");
		}
		getHouse().getGroups().add(g);
	}

	@Override
	public void onControllerByGroup(String groupId, String where) throws RestOperationException {
		Controller c = getController(where);
		if (c!=null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Controller [id:"+where+"] already exist.");
		}

		Group g = getGroup(groupId);
		if (g==null) {
			throw new RestOperationException(getUri(), Verb.PUT, "Label [Group:"+groupId+"] doesn't exist.");
		}

		g.getControllerList().add(createController(JSonTools.fromJson(json), where));
	}

	@Override
	public void onController(String where) throws RestOperationException, UnsupportedRestOperation {
		// Creation only in a physical group.
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onStatus(String name, String[] value)
			throws UnsupportedRestOperation, RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	private Controller createController(JSONObject json, String where) throws RestOperationException {
		Controller c = null;
		try {
//			c = service.createController(clazz, where); TODO géré ca
			c.fromJson(json);
			c.setWhere(new Where(where, where));
		} catch (Error e) {
			throw new RestOperationException(getUri(), Verb.PUT, "Controller JSON representation is wrong ["+json+"].");
		}
		return c;
	}

}
