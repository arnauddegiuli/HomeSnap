package com.adgsoftware.mydomo.webserver.rest.listener;

import java.util.Map;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
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
		if (l != null) {
			setResult(JSonTools.toJson(l));
		}
		else {
			setResult(JSonTools.formatNull());
		}
	}

	@Override
	public void onControllerByLabel(String labelId, String where) {
		Label l = getLabel(labelId);
		for (Controller<? extends Status> controller : l.getControllerList()) {
			if (controller.getWhere().equals(where)) {
				setResult(JSonTools.toJson(controller));
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
		for (Controller<? extends Status> controller : g.getControllerList()) {
			if (controller.getWhere().equals(where)) {
				setResult(JSonTools.toJson(controller));
				return;
			}
		}
		setResult(JSonTools.formatNull());
	}

	@Override
	public void onController(String where) {
		for (Group g : getHouse().getGroups()) {
			for (Controller<? extends Status> controller : g.getControllerList()) {
				if (controller.getWhere().equals(where)) {
					setResult(JSonTools.toJson(controller));
					return;
				}
			}
		}
		for (Label l : getHouse().getLabels()) {
			for (Controller<? extends Status> controller : l.getControllerList()) {
				if (controller.getWhere().equals(where)) {
					setResult(JSonTools.toJson(controller));
					return;
				}
			}
		}
		setResult(JSonTools.formatNull());
	}

	@Override
	public void onLightStatus(String where, LightStatus status) throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.GET);
	}
}
