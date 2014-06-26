package com.homesnap.engine.connector.openwebnet.counter;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import com.homesnap.engine.controller.counter.stateValue.CounterStatusValue;
import com.homesnap.engine.controller.light.stateValue.LightStatusValue;
import com.homesnap.engine.controller.what.StateValue;


// LIGHT
public enum CounterStatus {
	COUNTER_ON("0", CounterStatusValue.COUNTER_ON), // TODO manage speed 0 to 255!
	COUNTER_OFF("1", CounterStatusValue.COUNTER_OFF), // TODO manage speed!
	;

	private String code; // OpenWebNet code
	private StateValue value; // SnapHome code

	private CounterStatus(String code, StateValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public StateValue getValue() {
		return value;
	}

	public static CounterStatus fromValue(String code) {
		for (CounterStatus light: CounterStatus.values()) {
			if (light.getCode().equals(code))
				return light;
		}
		return null;
	}

	public static CounterStatus fromValue(StateValue name) {
		if (name == null) {
			return null;
		}
		for (CounterStatus light: CounterStatus.values()) {
			if (light.value.getValue().equals(name.getValue()))
				return light;
		}
		return null;
	}
}
