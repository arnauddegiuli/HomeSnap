package com.homesnap.webserver.rest.listener;

import java.util.Map;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.light.stateValue.LightStatusValue;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.Verb;
import com.homesnap.webserver.utils.JSonTools;



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
	public void onLightStatus(String where, LightStatusValue status) throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.GET);
	}
}
