package com.homesnap.engine.connector.openwebnet.automation;

import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.what.impl.UpDownState.UpDownValue;

public enum AutomationStatusConverter {
	AUTOMATION_UP("1", UpDownValue.UP),
	AUTOMATION_DOWN("2", UpDownValue.DOWN),
	AUTOMATION_STOP("0", UpDownValue.STOP);
	
	private String code;
	private UpDownValue value;
	private AutomationStatusConverter(String code, UpDownValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public UpDownValue getValue() {
		return value;
	}

	public static UpDownValue fromValue(String code) {
		for (AutomationStatusConverter automation: AutomationStatusConverter.values()) {
			if (automation.getCode().equals(code))
				return automation.value;
		}
		return null;
	}

	public static String fromValue(What name) {
		if (name == null) {
			return null;
		}
		
		for (AutomationStatusConverter automation: AutomationStatusConverter.values()) {
			if (automation.value.equals(name.getValue()))
				return automation.code;
		}
		return null;
	}
}
