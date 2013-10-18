package com.adgsoftware.mydomo.webserver.rest.listener;

import java.util.Map;

import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;

public class MyDomoRestListenerAbstract {

	private House house;
	private String result = "";
	private String uri;
	private Map<String, String[]> parameters;

	public MyDomoRestListenerAbstract(House house, String uri, Map<String, String[]> parameters) {
		super();
		this.house = house;
		this.uri = uri;
		this.parameters = parameters;
	}

	protected House getHouse() {
		return house;
	}
	
	public String getUri() {
		return uri;
	}

	public Map<String, String[]> getParameters() {
		return parameters;
	}

	protected Label getLabel(String labelId) {
		for (Label label : house.getLabels()) {
			if (label.getId().equals(labelId)) {
				return label;
			}
		}
		return null;
	}

	protected Group getGroup(String groupId) {
		for (Group group : house.getGroups()) {
			if (group.getId().equals(groupId)) {
				return group;
			}
		}
		return null;
	}

	public String getResult() {
		return result;
	}
	
	protected void setResult(String result) {
		this.result = result;
	}
}