package com.homesnap.engine.controller.automation;

import com.homesnap.engine.controller.what.StateValue;

/**
 * Enumeration of the possible values of the state name {@link AutomationStateName#STATUS}.
 * 
 * Generated by ControllerStateGenerator
 */
public enum AutomationStatusStateValue implements StateValue {
	
	UP,
	DOWN,
	STOP;
	
	@Override
	public String getValue() {
		return name().toLowerCase();
	}
}
