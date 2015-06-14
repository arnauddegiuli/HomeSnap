package com.homesnap.webserver.rest.house.listener;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Map;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;

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

	protected Controller getControllerByLabel(String labelId, String where) {
		Label l = getLabel(labelId);
		if (l != null) {
			for (Controller controller : l.getControllerList()) {
				if (controller.getWhere().getTo().equals(where)) {
					return controller;
				}
			}
		}
		return null;
	}
	
	protected Controller getController(String where) {
		for (Group g : getHouse().getGroups()) {
			for (Controller controller : g.getControllerList()) {
				if (controller.getWhere().getTo().equals(where)) {
					return controller;
				}
			}
		}
		for (Label l : getHouse().getLabels()) {
			for (Controller controller : l.getControllerList()) {
				if (controller.getWhere().getTo().equals(where)) {
					return controller;
				}
			}
		}
		return null;
	}

	protected Controller getControllerByGroup(String groupId, String where) {
		Group g = getGroup(groupId);
		if (g != null) {
			for (Controller controller : g.getControllerList()) {
				if (controller.getWhere().getTo().equals(where)) {
					return controller;
				}
			}
		}
		return null;
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
