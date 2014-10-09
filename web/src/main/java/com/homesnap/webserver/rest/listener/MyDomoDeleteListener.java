package com.homesnap.webserver.rest.listener;

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
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.Verb;
import com.homesnap.webserver.utils.JSonTools;


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
			l.clear();
			setResult(JSonTools.toJson(l));
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
					setResult(JSonTools.toJson(controller));
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
			g.clear();
			setResult(JSonTools.toJson(g));
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
					setResult(JSonTools.toJson(controller));
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
}
