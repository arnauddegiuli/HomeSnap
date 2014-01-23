package com.adgsoftware.mydomo.engine.controller.automation;

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

import com.adgsoftware.mydomo.engine.connector.ControllerType;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Automation extends Controller<Automation.AutomationStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// AUTOMATION
	public enum AutomationStatus implements Status {
		AUTOMATION_STOP("0"), AUTOMATION_UP("1"), AUTOMATION_DOWN("2");
		
		private String code;
		private AutomationStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	
	@Override
	public String getWho() {
		return ControllerType.WHO_AUTOMATION;
	}
	
	@Override
	public AutomationStatus getStatus(String code) {
		if (code == null) 
			return null;
		for (AutomationStatus status : AutomationStatus.values()) {
			if (code.equals(status.getCode())) {
				return status;
			}
		}	
		return null;
	}
	

	@Override
	public JSONObject toJson() {
		Status what = getWhat();
		String strStatus = null;
		if (AutomationStatus.AUTOMATION_STOP == what) {
			strStatus = "stop";
		} else if (AutomationStatus.AUTOMATION_DOWN == what) {
			strStatus = "down";
		} else if (AutomationStatus.AUTOMATION_UP == what) {
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
		setWhere(jsonObject.getString("where"));
		Object what = jsonObject.get("what");
		if ("up".equals(what)) {
			setWhat(AutomationStatus.AUTOMATION_UP);	
		} else if ("down".equals(what)) {
			setWhat(AutomationStatus.AUTOMATION_DOWN);
		} else if ("stop".equals(what)) {
			setWhat(AutomationStatus.AUTOMATION_STOP);
		} else {
			// TODO message: status unknown or undefined
		}
		setTitle(jsonObject.getString("title"));
		setDescription(jsonObject.getString("description"));
	}
}
