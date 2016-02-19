         package com.homesnap.webserver.rest.recorder.utils;

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

import com.homesnap.recorder.Record;

public class JSonTools {

	public final static String toJson(Record record) {
		if (record == null) {
			return JSONObject.NULL.toString();
		} else {
			return String.valueOf(record.toJson());
		}

	}

	public final static String toJson(List<Record> records) {
		StringBuilder sb = new StringBuilder("[");
		for (Record record : records) {
			sb.append(JSonTools.toJson(record));
			sb.append(",");
		}
		if (sb.length()>1) {
			sb.setLength(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
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
