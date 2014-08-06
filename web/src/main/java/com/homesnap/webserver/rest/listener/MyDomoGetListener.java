package com.homesnap.webserver.rest.listener;

import java.util.Map;

import com.homesnap.engine.house.House;
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
								"http[s]://server:port/house/labels/{labelId}/controller/{id}\n" +
								"\n" +
								"http[s]://server:port/house/groups\n" +
								"http[s]://server:port/house/groups/group?id={id}\n" +
								"http[s]://server:port/house/groups/{groupId}\n" +
								"http[s]://server:port/house/groups/{groupId}/{where}\n" +
								"http[s]://server:port/house/groups/{groupId}/controller?id={id}\n" +
								"http[s]://server:port/house/groups/{groupId}/controller/{id}\n" +
								"\n" +
								"http[s]://server:port/house/controllers/{id}\n" +
								"http[s]://server:port/house/controllers/controller?id={id}\n" +
								"http[s]://server:port/house/controllers/controller/{id}\n"
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
		setResult(JSonTools.toJson(getHouse().getLabels()));
	}

	@Override
	public void onLabel(String labelId) {
		setResult(JSonTools.toJson(getLabel(labelId)));
	}

	@Override
	public void onControllerByLabel(String labelId, String where) {
		setResult(JSonTools.toJson(getControllerByLabel(labelId, where)));
	}

	@Override
	public void onGroupList() {
		setResult(JSonTools.toJsonGroups(getHouse().getGroups()));
	}

	@Override
	public void onGroup(String groupId) {
		setResult(JSonTools.toJson(getGroup(groupId)));
	}

	@Override
	public void onControllerByGroup(String groupId, String where) {
		setResult(JSonTools.toJson(getControllerByGroup(groupId, where)));
	}

	@Override
	public void onController(String where) {
		setResult(JSonTools.toJson(getController(where)));
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
