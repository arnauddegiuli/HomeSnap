         package com.homesnap.webserver.rest.house.utils;

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


import java.util.List;

import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.house.MissingParameterRestOperation;
import com.homesnap.webserver.rest.house.RestOperationException;
import com.homesnap.webserver.rest.house.UnsupportedRestOperation;

public class JSonTools {

	public final static String ERROR = "error";
	public final static String ERROR_CLASSNAME = "className";
	public final static String ERROR_MESSAGE = "message";
	
	
	public final static String toJson(House house) {
		if (house == null) {
			return JSONObject.NULL.toString();
		} else {
			return String.valueOf(house.toJson());
		}
	}
	
	public final static String toJson(Group group) {
		if (group == null) {
			return JSONObject.NULL.toString();
		} else {
			return String.valueOf(group.toJson());
		}
	}

	public final static String toJsonGroups(List<Group> groups) {
		StringBuilder sb = new StringBuilder("[");
		for (Group group : groups) {
			sb.append(JSonTools.toJson(group));
			sb.append(",");
		}
		if (sb.length()>1) {
			sb.setLength(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}

	public final static String toJson(Label label) {
		if (label == null) {
			return JSONObject.NULL.toString();
		} else {
			return String.valueOf(label.toJson());
		}

	}

	public final static String toJson(List<Label> labels) {
		StringBuilder sb = new StringBuilder("[");
		for (Label label : labels) {
			sb.append(JSonTools.toJson(label));
			sb.append(",");
		}
		if (sb.length()>1) {
			sb.setLength(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}

	public final static String toJson(Controller controller) {
		if (controller == null) {
			return JSONObject.NULL.toString();
		} else {
			return String.valueOf(controller.toJson());
		}
	}

	public final static String toJson(MissingParameterRestOperation e) {
		return formatException(e);
	}

	public final static String toJson(UnsupportedRestOperation e) {
		return formatException(e);
	}

	public final static String toJson(RestOperationException e) {
		return formatException(e);
	}

	private final static String formatException(Exception e) {
		return "{" + ERROR + ": [{" + ERROR_CLASSNAME + ":'" + JSONObject.quote(e.getClass().getSimpleName()) + "', " + ERROR_MESSAGE + " :'" + JSONObject.quote(e.getMessage())+ "'}]}";
	}

	public final static String formatNull() {
		return JSONObject.NULL.toString();
	}

	public final static JSONObject fromJson(String value)  {
		if (formatNull().equals(value))
			return null;
		return new JSONObject(value);
	}
}
