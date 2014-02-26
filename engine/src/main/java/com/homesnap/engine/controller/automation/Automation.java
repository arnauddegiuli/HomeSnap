package com.homesnap.engine.controller.automation;

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


import org.json.JSONObject;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.light.Light.LightStateValue;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;


public class Automation extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// AUTOMATION
	public enum AutomationState implements StateValue {
		AUTOMATION_STOP("stop"), AUTOMATION_UP("up"), AUTOMATION_DOWN("down");
		
		private String value;
		private AutomationState(String code) {
			this.value = code;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	@Override
	public Who getWho() {
		return Who.AUTOMATION;
	}

	public AutomationState getStatus() {
		return (AutomationState) get(StateName.STATUS);	
	}

	public void setStatus(AutomationState status) {
		set(StateName.STATUS, status);	
	}

	@Override
	protected void initStateTypes() {
		declareState(StateName.STATUS, LightStateValue.class);
	}

	@Override
	public JSONObject toJson() {
		AutomationState what = getStatus();
		String strStatus = null;
		if (AutomationState.AUTOMATION_STOP == what) {
			strStatus = "stop";
		} else if (AutomationState.AUTOMATION_DOWN == what) {
			strStatus = "down";
		} else if (AutomationState.AUTOMATION_UP == what) {
			strStatus = "up";
		}
		JSONObject lightJson = new JSONObject();
		lightJson.put("where", getWhere())
				 .put("what", strStatus)
				 .put("title", getTitle())
				 .put("description", getDescription());
		return lightJson; // TODO labels are lost...
	}

	@Override
	public void fromJson(JSONObject jsonObject) {
		setWhere(new Where(jsonObject.getString("where"),jsonObject.getString("where")));
		Object what = jsonObject.get("what");
		if ("up".equals(what)) {
			set(StateName.STATUS, AutomationState.AUTOMATION_UP);	
		} else if ("down".equals(what)) {
			set(StateName.STATUS, AutomationState.AUTOMATION_DOWN);
		} else if ("stop".equals(what)) {
			set(StateName.STATUS, AutomationState.AUTOMATION_STOP);
		} else {
			// TODO message: status unknown or undefined
		}
		setTitle(jsonObject.getString("title"));
		setDescription(jsonObject.getString("description"));
	}
}
