package com.homesnap.engine.controller.light;

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

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

public class Light extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;

	public enum LightStateValue implements StateValue {
		LIGHT_OFF, // TODO manage speed 0 to 255!
		LIGHT_ON, // TODO manage speed!
		LIGHT_ON_20_PERCENT,
		LIGHT_ON_30_PERCENT,
		LIGHT_ON_40_PERCENT,
		LIGHT_ON_50_PERCENT,
		LIGHT_ON_60_PERCENT,
		LIGHT_ON_70_PERCENT,
		LIGHT_ON_80_PERCENT,
		LIGHT_ON_90_PERCENT,
		LIGHT_ON_100_PERCENT,
		LIGHT_ON_DURING_1_MIN,
		LIGHT_ON_DURING_2_MIN,
		LIGHT_ON_DURING_3_MIN,
		LIGHT_ON_DURING_4_MIN,
		LIGHT_ON_DURING_5_MIN,
		LIGHT_ON_DURING_15_MIN,
		LIGHT_ON_DURING_30_SEC,
		LIGHT_ON_DURING_HALF_SEC,
		LIGHT_ERROR,
		LIGHT_ON_BLINKING_HALF_SEC,
		LIGHT_ON_BLINKING_1_SEC,
		LIGHT_ON_BLINKING_1_AND_HALF_SEC,
		LIGHT_ON_BLINKING_2_SEC,
		LIGHT_ON_BLINKING_2_AND_HALF_SEC,
		LIGHT_ON_BLINKING_3_SEC,
		LIGHT_ON_BLINKING_3_AND_HALF_SEC,
		LIGHT_ON_BLINKING_4_SEC,
		LIGHT_ON_BLINKING_4_AND_HALF_SEC,
		LIGHT_ON_BLINKING_5_SEC,
		LIGHT_ON_UP_ONE_LEVEL, // TODO manage speed!
		LIGHT_OFF_ONE_LEVEL, // TODO manage speed!

		LIGHT_FORCE_ON,
		LIGHT_FORCE_OFF;

		@Override
		public String getValue() {
			return name();
		}
	}

	public Light() {
	}

	@Override
	public Who getWho() {
		return Who.LIGHT;
	}

	@Override
	public JSONObject toJson() {
		JSONObject lightJson = super.toJson();
		StateValue what = getStatus();
		String strStatus = (what == null ? null : LightStateValue.LIGHT_ON == what ? "on" : "off");
		lightJson.put("where", getWhere())
				 .put("what", strStatus);
		return lightJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		if (jsonObject == null)
			return;

		super.fromJson(jsonObject);
		Where w = new Where(jsonObject.getString("where"), jsonObject.getString("where"));
		setWhere(w); // TODO  manage where better
		Object what = jsonObject.get("what");
		if ("on".equals(what)) {
			set(StateName.STATUS,LightStateValue.LIGHT_ON);	
		} else if ("off".equals(what)) {
			set(StateName.STATUS,LightStateValue.LIGHT_OFF);
		} else {
			throw new UnmarshalException("Error when deserialized status from JSON object (" + jsonObject.toString() + ")");
		}
	}
	
	@Override
	protected void initStateTypes() {
		declareState(StateName.STATUS, LightStateValue.class);
	}
	
	
	public LightStateValue getStatus() {
		return (LightStateValue) get(StateName.STATUS);
	}

	public void setStatus(LightStateValue status) {
		set(StateName.STATUS, status);
	}
}