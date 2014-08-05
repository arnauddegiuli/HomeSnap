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
import com.homesnap.webserver.utils.JSonTools;



public class MyDomoGetListener extends MyDomoRestListenerAbstract implements MyDomoRestAPI {

	
	public static final String REST_API =	"Usage: http[s]://server:port/house\n" +
								"http[s]://server:port/house/labels\n" +
								"http[s]://server:port/house/labels/{labelId}\n" +
								"http[s]://server:port/house/labels/label?id={id}\n" +
								"http[s]://server:port/house/labels/{labelId}/{where}\n" +
								"http[s]://server:port/house/labels/{labelId}/controller?id={id}\n" +
								"\n" +
								"http[s]://server:port/house/groups\n" +
								"http[s]://server:port/house/groups/group?id={id}\n" +
								"http[s]://server:port/house/groups/{groupId}\n" +
								"http[s]://server:port/house/groups/{groupId}/{where}\n" +
								"http[s]://server:port/house/groups/{groupId}/controller?id={id}\n" +
								"\n" +
								"http[s]://server:port/house/controllers/{id}\n" +
								"http[s]://server:port/house/controllers/controller?id={id}\n"
								;	

	public MyDomoGetListener(House house, String uri, Map<String, String[]> parameters) {
		super(house, uri, parameters);
	}

	@Override
	public void onHouse() {
		setResult(JSonTools.toJson(getHouse()));
	}

	@Override
	public void onLabelList() {
		StringBuilder sb = new StringBuilder("[");
		for (Label label : getHouse().getLabels()) {
			sb.append(JSonTools.toJson(label));
			sb.append(",");
		}
		sb.setLength(sb.length()-1);
		sb.append("]");
		setResult(sb.toString());
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
			if (controller.getWhere().getTo().equals(where)) {
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
			} else if (controller.getWhere().getTo().equals(where)) {
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
				if (controller.getWhere().getTo().equals(where)) {
					setResult(controller.toJson().toString());
					return;
				}
			}
		}
		for (Label l : getHouse().getLabels()) {
			for (Controller controller : l.getControllerList()) {
				if (controller.getWhere().getTo().equals(where)) {
					setResult(controller.toJson().toString());
					return;
				}
			}
		}
		setResult(JSonTools.formatNull());
	}

	@Override
	public void onStatus(String name, String[] value)
			throws UnsupportedRestOperation, RestOperationException,
			MissingParameterRestOperation {
		// TODO Auto-generated method stub
		for (int i = 0; i < value.length; i++) {
			System.out.println("Status [name:" + name + "] - [value_"+ i + ":" + value[i] + "]");	
		}
	}
}
