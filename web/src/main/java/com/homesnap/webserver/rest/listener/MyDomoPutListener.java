package com.homesnap.webserver.rest.listener;

import java.util.Map;

import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.MissingParameterRestOperation;
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

	@Override
	public void onStatus(String name, String[] value)
			throws UnsupportedRestOperation, RestOperationException,
			MissingParameterRestOperation {
		// TODO Auto-generated method stub
		for (int i = 0; i < value.length; i++) {
			System.out.println("Status [name:" + name + "] - [value_"+ i + ":" + value[i] + "]");	
		}
		
		// TODO gérer le status de tous ce qui a été sélectionné
	}

	private void updateController(Controller c, String errorMessage) throws RestOperationException {
		if (c != null) {
			try {
				JSONObject j = JSonTools.fromJson(json);
				c.fromJson(j);
			} catch (Error e) {
				throw new RestOperationException(getUri(), Verb.PUT, "Controller JSON representation is wrong ["+json+"].");
			}	
		} else {
			throw new RestOperationException(getUri(), Verb.PUT, errorMessage);
		}
	}
}
