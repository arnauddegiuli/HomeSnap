package com.homesnap.engine.connector.openwebnet.automation;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.homesnap.engine.controller.automation.AutomationStatusStateValue;
import com.homesnap.engine.controller.what.StateValue;

public enum AutomationStatusConverter {
	AUTOMATION_UP("1", AutomationStatusStateValue.UP),
	AUTOMATION_DOWN("2", AutomationStatusStateValue.DOWN),
	AUTOMATION_STOP("0", AutomationStatusStateValue.STOP);
	
	private String code;
	private StateValue value;
	private AutomationStatusConverter(String code, StateValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public StateValue getValue() {
		return value;
	}

	public static StateValue fromValue(String code) {
		for (AutomationStatusConverter automation: AutomationStatusConverter.values()) {
			if (automation.getCode().equals(code))
				return automation.value;
		}
		return null;
	}

	public static String fromValue(StateValue name) {
		if (name == null) {
			return null;
		}
		for (AutomationStatusConverter automation: AutomationStatusConverter.values()) {
			if (automation.value.getValue().equals(name.getValue()))
				return automation.code;
		}
		return null;
	}
}
