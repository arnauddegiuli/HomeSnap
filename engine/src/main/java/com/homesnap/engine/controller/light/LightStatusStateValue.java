package com.homesnap.engine.controller.light;

import com.homesnap.engine.controller.what.StateValue;

/**
 * Enumeration of the possible values of the state name {@link LightStateName#STATUS}.
 * 
 * Generated by ControllerStateGenerator
 */
public enum LightStatusStateValue implements StateValue {
	
	ON,
	OFF,
	BLINKING;
	
	@Override
	public String getValue() {
		return name().toLowerCase();
	}
}
