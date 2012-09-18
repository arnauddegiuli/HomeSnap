package com.adgsoftware.mydomo.engine.controller.outlet;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Outlet extends Controller<Outlet.AutomationStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// OUTLET
	public enum AutomationStatus implements Status {
		OUTLET_OFF("0"),
		OUTLET_ON("1");
		
		private String code = "";
		private AutomationStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public Outlet() {
	}
	
	@Override
	public String getWho() {
		return Command.WHO_POWER_MANAGEMENT;
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