package com.homesnap.webserver.rest.listener;

import java.util.Map;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.Verb;


public class MyDomoDeleteListener extends MyDomoRestListenerAbstract implements MyDomoRestAPI {

	
	public MyDomoDeleteListener(House house, String uri, Map<String, String[]> parameters) {
		super(house, uri, parameters);
	}

	@Override
	public void onHouse() {
		getHouse().getGroups().clear();
		getHouse().getLabels().clear();
	}

	@Override
	public void onLabelList() {
		getHouse().getLabels().clear();
	}

	@Override
	public void onLabel(String labelId) throws RestOperationException {
		Label l = getLabel(labelId);
		if (l != null) {
			getHouse().getLabels().remove(l);
		} else {
			throw new RestOperationException(getUri(), Verb.DELETE, "Label [id:"+labelId+"] not found.");
		}
	}

	@Override
	public void onControllerByLabel(String labelId, String where) throws RestOperationException {
		Label l = getLabel(labelId);
		if (l != null) {
			for (Controller controller : l.getControllerList()) {
				if(controller.getWhere().getTo().equals(where)) {
					l.remove(controller);
					return;
				}
			}
			throw new RestOperationException(getUri(), Verb.DELETE, "Controller [id:"+where+"] not found in Label[id:" + labelId + "].");
		} else {
			throw new RestOperationException(getUri(), Verb.DELETE, "Label [id:"+labelId+"] not found.");
		}
	}

	@Override
	public void onGroupList() {
		getHouse().getGroups().clear();
	}

	@Override
	public void onGroup(String groupId) throws RestOperationException {
		Group g = getGroup(groupId);
		if (g != null) {
			getHouse().getGroups().remove(g);
		} else {
			throw new RestOperationException(getUri(), Verb.DELETE, "Group [id:"+groupId+"] not found.");
		}
	}

	@Override
	public void onControllerByGroup(String groupId, String where) throws RestOperationException {
		Group g = getGroup(groupId);
		if (g != null) {
			for (Controller controller : g.getControllerList()) {
				if(controller.getWhere().getTo().equals(where)) {
					g.remove(controller);
					return;
				}
			}
			throw new RestOperationException(getUri(), Verb.DELETE, "Controller [id:"+where+"] not found in Group[id:" + groupId + "].");
		} else {
			throw new RestOperationException(getUri(), Verb.DELETE, "Group [id:"+groupId+"] not found.");
		}
	}

	@Override
	public void onController(String where) throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.DELETE);
	}

	@Override
	public void onStatus(String name, String[] value)
			throws UnsupportedRestOperation, RestOperationException,
			MissingParameterRestOperation {
		// TODO Auto-generated method stub
		
	}
}
