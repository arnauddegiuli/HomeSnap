package com.adgsoftware.mydomo.webserver.rest.listener;

import java.util.Map;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStateValue;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.webserver.rest.MyDomoRestAPI;
import com.adgsoftware.mydomo.webserver.rest.UnsupportedRestOperation;
import com.adgsoftware.mydomo.webserver.rest.Verb;
import com.adgsoftware.mydomo.webserver.utils.JSonTools;



public class MyDomoGetListener extends MyDomoRestListenerAbstract implements MyDomoRestAPI {

	
	public static final String REST_API =	"Usage: http[s]://server:port/house\n" +
								"http[s]://server:port/house/labels\n" +
								"http[s]://server:port/house/labels/labelId\n" +
								"http[s]://server:port/house/labels/label?id=id\n" +
								"http[s]://server:port/house/labels/labelId/where\n" +
								"http[s]://server:port/house/labels/labelId/controller?id=id\n";	

	public MyDomoGetListener(House house, String uri, Map<String, String[]> parameters) {
		super(house, uri, parameters);
	}

	@Override
	public void onHouse() {
		setResult(JSonTools.toJson(getHouse()));
	}

	@Override
	public void onLabelList() {
		// TODO serialize list of label
	}

	@Override
	public void onLabel(String labelId) {
		Label l = getLabel(labelId);
		setResult(JSonTools.toJson(l));
	}

	@Override
	public void onControllerByLabel(String labelId, String where) {
		Label l = getLabel(labelId);
		for (Controller controller : l.getControllerList()) {
			if (controller.getWhere().equals(where)) {
				setResult(controller.toJson().toString());
				return;
			}
		}
		setResult(JSonTools.formatNull());
	}

	@Override
	public void onGroupList() {
		// TODO serialize list of label
	}

	@Override
	public void onGroup(String groupId) {
		setResult(JSonTools.toJson(getGroup(groupId)));
	}

	@Override
	public void onControllerByGroup(String groupId, String where) {
		Group g = getGroup(groupId);
		for (Controller controller : g.getControllerList()) {
			if (controller.getWhere() == null) {
				// TODO log warning!
			} else if (controller.getWhere().equals(where)) {
				setResult(controller.toJson().toString());
				return;
			}
		}
		setResult(JSonTools.formatNull());
	}

	@Override
	public void onController(String where) {
		for (Group g : getHouse().getGroups()) {
			for (Controller controller : g.getControllerList()) {
				if (controller.getWhere().equals(where)) {
					setResult(controller.toJson().toString());
					return;
				}
			}
		}
		for (Label l : getHouse().getLabels()) {
			for (Controller controller : l.getControllerList()) {
				if (controller.getWhere().equals(where)) {
					setResult(controller.toJson().toString());
					return;
				}
			}
		}
		setResult(JSonTools.formatNull());
	}

	@Override
	public void onLightStatus(String where, LightStateValue status) throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.GET);
	}
}
