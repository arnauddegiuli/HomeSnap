package com.homesnap.recorder;

/*
 * #%L
 * HomeSnapRecorder
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Record {
	
	private String PROBE_ID_KEY= "probeID";
	private String VALUE_KEY= "value";
	private String DATETIME_KEY= "dateTime";
	private String TYPE_KEY= "type";
	
	private String probeId;
	private String value;
	private Timestamp date;
	private String type;
	public String getProbeId() {
		return probeId;
	}
	public void setProbeId(String probeId) {
		this.probeId = probeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public JSONObject toJson() {
		JSONObject oJson = new JSONObject();
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss");

		oJson.put(PROBE_ID_KEY, probeId);
		oJson.put(VALUE_KEY, value);
		oJson.put(DATETIME_KEY, sp.format(new Date(getDate().getTime())));
		oJson.put(TYPE_KEY, type);
		
		return oJson;
	}
	
	public void fromJson(JSONObject value) {
		
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss");
		
		setProbeId(value.getString(PROBE_ID_KEY));
		setValue(value.getString(VALUE_KEY));
		try {
			setDate(new Timestamp(sp.parse(value.getString(DATETIME_KEY)).getTime()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setType(value.getString(TYPE_KEY));
	}


}
