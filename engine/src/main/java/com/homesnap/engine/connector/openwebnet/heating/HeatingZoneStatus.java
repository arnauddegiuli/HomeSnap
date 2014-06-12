package com.homesnap.engine.connector.openwebnet.heating;

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

import com.homesnap.engine.controller.heating.HeatingZone;
import com.homesnap.engine.controller.what.StateValue;

public enum HeatingZoneStatus {

	HEATING_MOD("1", HeatingZone.HeatingZoneStatus.HEATING_MODE),
	HEATING_OFF("303", HeatingZone.HeatingZoneStatus.OFF),
	THERMAL_PROTECTION("202", HeatingZone.HeatingZoneStatus.THERMAL_PROTECTION);

	private String code;
	private StateValue value;

	private HeatingZoneStatus(String code, StateValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public StateValue getValue() {
		return value;
	}

	public static HeatingZoneStatus fromValue(String code) {
		for (HeatingZoneStatus zone: HeatingZoneStatus.values()) {
			if (zone.getCode().equals(code))
				return zone;
		}
		return null;
	}

	public static HeatingZoneStatus fromValue(StateValue name) {
		if (name == null) {
			return null;
		}
		for (HeatingZoneStatus zone: HeatingZoneStatus.values()) {
			if (zone.value.getValue().equals(name.getValue()))
				return zone;
		}
		return null;
	}
}
