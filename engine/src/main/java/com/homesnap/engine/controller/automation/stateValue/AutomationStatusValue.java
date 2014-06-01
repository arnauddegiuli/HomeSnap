package com.homesnap.engine.controller.automation.stateValue;

import com.homesnap.engine.controller.what.StateValue;

// AUTOMATION
public enum AutomationStatusValue implements StateValue {
	STOP, UP, DOWN;
	@Override
	public String getValue() {
		return name();
	}
}
