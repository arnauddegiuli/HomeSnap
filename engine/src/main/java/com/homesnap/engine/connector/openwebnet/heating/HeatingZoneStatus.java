package com.homesnap.engine.connector.openwebnet.heating;

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
