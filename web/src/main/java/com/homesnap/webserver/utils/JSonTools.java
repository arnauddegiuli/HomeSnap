         package com.homesnap.webserver.utils;

/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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


import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;

public class JSonTools {

	public final static String toJson(House house) {
		if (house == null) {
			return JSONObject.NULL.toString();
		} else {
			return house.toJson().toString();
		}
	}
	
	public final static String toJson(Group group) {
		if (group == null) {
			return JSONObject.NULL.toString();
		} else {
			return group.toJson().toString();
		}
	}

	public final static String toJson(Label label) {
		if (label == null) {
			return JSONObject.NULL.toString();
		} else {
			return label.toJson().toString();
		}

	}

	public final static String toJson(Controller controller) {
		if (controller == null) {
			return JSONObject.NULL.toString();
		} else {
			return controller.toJson().toString();
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
		return "[{error:'" + JSONObject.quote(e.getClass().getSimpleName()) + "', message:'" + JSONObject.quote(e.getMessage())+ "'}]";
	}

	public final static String formatNull() {
		return JSONObject.NULL.toString();
	}
}
