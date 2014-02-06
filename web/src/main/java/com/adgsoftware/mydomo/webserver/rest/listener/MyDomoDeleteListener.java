package com.adgsoftware.mydomo.webserver.rest.listener;

import java.util.Map;

import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.engine.oldcontroller.Controller;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;
import com.adgsoftware.mydomo.engine.oldcontroller.light.Light.LightStatus;
import com.adgsoftware.mydomo.webserver.rest.MyDomoRestAPI;
import com.adgsoftware.mydomo.webserver.rest.RestOperationException;
import com.adgsoftware.mydomo.webserver.rest.UnsupportedRestOperation;
import com.adgsoftware.mydomo.webserver.rest.Verb;


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
			for (Controller<? extends Status> controller : l.getControllerList()) {
				if(controller.getWhere().equals(where)) {
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
			for (Controller<? extends Status> controller : g.getControllerList()) {
				if(controller.getWhere().equals(where)) {
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
	public void onLightStatus(String where, LightStatus status) throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.DELETE);
	}
}
