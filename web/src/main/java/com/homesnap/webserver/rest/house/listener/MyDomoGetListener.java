package com.homesnap.webserver.rest.house.listener;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
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

import com.homesnap.engine.house.House;
import com.homesnap.webserver.rest.house.MyDomoRestAPI;
import com.homesnap.webserver.rest.house.utils.JSonTools;



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
}
