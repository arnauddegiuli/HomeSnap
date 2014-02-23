package com.adgsoftware.mydomo.engine.connector.openwebnet.automation;

import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.what.StateValue;

public enum AutomationStatus {
	AUTOMATION_UP("1", Automation.AutomationState.AUTOMATION_UP),
	AUTOMATION_DOWN("2", Automation.AutomationState.AUTOMATION_DOWN),
	AUTOMATION_STOP("0", Automation.AutomationState.AUTOMATION_STOP);
	
	private String code;
	private StateValue value;
	private AutomationStatus(String code, StateValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public StateValue getValue() {
		return value;
	}

	public static AutomationStatus fromValue(String code) {
		for (AutomationStatus automation: AutomationStatus.values()) {
			if (automation.getCode().equals(code))
				return automation;
		}
		return null;
	}

	public static AutomationStatus fromValue(StateValue name) {
		if (name == null) {
			return null;
		}
		for (AutomationStatus automation: AutomationStatus.values()) {
			if (automation.value.getValue().equals(name.getValue()))
				return automation;
		}
		return null;
	}
}
