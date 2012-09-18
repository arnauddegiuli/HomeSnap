package com.adgsoftware.mydomo.engine.controller.automation;

import com.adgsoftware.mydomo.engine.Command;
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
		return Command.WHO_AUTOMATION;
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
}
