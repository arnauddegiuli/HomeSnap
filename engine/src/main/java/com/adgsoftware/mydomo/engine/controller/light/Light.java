package com.adgsoftware.mydomo.engine.controller.light;

/*
 * #%L
 * MyDomoEngine
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


import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.connector.ControllerType;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Light extends Controller<Light.LightStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// LIGHT
	public enum LightStatus implements Status {
		LIGHT_OFF("0"), // TODO manage speed 0 to 255!
		LIGHT_ON("1"), // TODO manage speed!
		LIGHT_ON_20_PERCENT("2"),
		LIGHT_ON_30_PERCENT("3"),
		LIGHT_ON_40_PERCENT("4"),
		LIGHT_ON_50_PERCENT("5"),
		LIGHT_ON_60_PERCENT("6"),
		LIGHT_ON_70_PERCENT("7"),
		LIGHT_ON_80_PERCENT("8"),
		LIGHT_ON_90_PERCENT("9"),
		LIGHT_ON_100_PERCENT("10"),
		LIGHT_ON_DURING_1_MIN("11"),
		LIGHT_ON_DURING_2_MIN("12"),
		LIGHT_ON_DURING_3_MIN("13"),
		LIGHT_ON_DURING_4_MIN("14"),
		LIGHT_ON_DURING_5_MIN("15"),
		LIGHT_ON_DURING_15_MIN("16"),
		LIGHT_ON_DURING_30_SEC("17"),
		LIGHT_ON_DURING_HALF_SEC("18"),
		LIGHT_ERROR("19"),
		LIGHT_ON_BLINKING_HALF_SEC("20"),
		LIGHT_ON_BLINKING_1_SEC("21"),
		LIGHT_ON_BLINKING_1_AND_HALF_SEC("22"),
		LIGHT_ON_BLINKING_2_SEC("23"),
		LIGHT_ON_BLINKING_2_AND_HALF_SEC("24"),
		LIGHT_ON_BLINKING_3_SEC("25"),
		LIGHT_ON_BLINKING_3_AND_HALF_SEC("26"),
		LIGHT_ON_BLINKING_4_SEC("27"),
		LIGHT_ON_BLINKING_4_AND_HALF_SEC("28"),
		LIGHT_ON_BLINKING_5_SEC("29"),
		LIGHT_ON_UP_ONE_LEVEL("30"), // TODO manage speed!
		LIGHT_OFF_ONE_LEVEL("31"), // TODO manage speed!

		LIGHT_FORCE_ON("1000#1"),
		LIGHT_FORCE_OFF("1000#0");
		private String code = "";
		private LightStatus(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public Light() {
	}

	@Override
	public String getWho() {
		return ControllerType.WHO_LIGHTING;
	}

	@Override
	public LightStatus getStatus(String code) {
		if (code == null) 
			return null;
		for (LightStatus status : LightStatus.values()) {
			if (code.equals(status.getCode())) {
				return status;
			}
		}	
		return null;
	}

	@Override
	public JSONObject toJson() {
		JSONObject lightJson = super.toJson();
		Status what = getWhat();
		String strStatus = (what == null ? null : LightStatus.LIGHT_ON == what ? "on" : "off");
		lightJson.put("where", getWhere())
				 .put("what", strStatus);
		return lightJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		if (jsonObject == null)
			return;

		super.fromJson(jsonObject);
		setWhere(jsonObject.getString("where"));
		Object what = jsonObject.get("what");
		if ("on".equals(what)) {
			setWhat(LightStatus.LIGHT_ON);	
		} else if ("off".equals(what)) {
			setWhat(LightStatus.LIGHT_OFF);
		} else {
			throw new UnmarshalException("Error when deserialized status from JSON object (" + jsonObject.toString() + ")");
		}
	}
}