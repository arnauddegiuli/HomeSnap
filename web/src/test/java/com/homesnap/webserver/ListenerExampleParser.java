package com.homesnap.webserver;

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

import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;


public class ListenerExampleParser implements MyDomoRestAPI {

	public boolean success = false;
	
	@Override
	public void onHouse() {
		System.out.println("Demande la maison");
		success = true;
	}

	@Override
	public void onLabelList() {
		System.out.println("Demande la liste des labels");
		success = true;
	}

	@Override
	public void onLabel(String labelId) {
		System.out.println("Demande le label [id:" + labelId + "]");
		success = true;
	}

	@Override
	public void onControllerByLabel(String labelId, String where) {
		System.out.println("Demande le controller [labelid:" + labelId + " - where:" + where + "]");
		success = true;
	}

	@Override
	public void onGroupList() {
		System.out.println("Demande la liste des groups");
		success = true;
	}

	@Override
	public void onGroup(String groupId) {
		System.out.println("Demande le group [id:" + groupId + "]");
		success = true;
	}

	@Override
	public void onControllerByGroup(String groupId, String where) {
		System.out.println("Demande le controller [groupId:" + groupId + " - where:" + where + "]");
		success = true;
	}

	@Override
	public void onController(String where) {
		System.out.println("Demande le controller [where:" + where + "]");
		success = true;
	}

	@Override
	public void onStatus(String name, String[] value)
			throws UnsupportedRestOperation, RestOperationException,
			MissingParameterRestOperation {
		for (int i = 0; i < value.length; i++) {
			System.out.println("Status [name:" + name + "] - [value_"+ i + ":" + value[i] + "]");	
		}
		success = true;
	}
}
