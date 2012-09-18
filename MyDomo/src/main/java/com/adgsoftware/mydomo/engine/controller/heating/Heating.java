package com.adgsoftware.mydomo.engine.controller.heating;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Heating extends Controller<Heating.HeatingStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// HEATING
	public enum HeatingStatus implements Status {
		HEATING_CONDITIONING_ON("0"),
		HEATING_HEATING_ON("1"),
		HEATING_ANTI_FREEZE_ON("102"),
		HEATING_THERMAL_PROTECTION("202"), // TODO What means this???
		HEATING_HEATING_OFF("103"),
		HEATING_CONDITIONING_OFF("203"),
		HEATING_OFF("303"),
		HEATING_HEATING_MANUAL_ADJUSTMENT_MODE("110"),
		HEATING_CONDITIONING_MANUAL_ADJUSTMENT_MODE("210"),
		HEATING_GENERIC_MANUAL_ADJUSTMENT_MODE("310"),
		HEATING_HEATING_PROGRAMMING_MODE("111"),
		HEATING_CONDITIONING_PROGRAMMING_MODE("211"),
		HEATING_GENERIC_PROGRAMMING_MODE("311"),
		HEATING_HEATING_HOLIDAY_DAILY("115"),
		HEATING_CONDITIONING_HOLIDAY_DAILY("215"),
		HEATING_GENERIC_HOLIDAY_DAILY("315");
		
		private String code = "";
		private HeatingStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	@Override
	public String getWho() {
		return Command.WHO_HEATING_ADJUSTMENT;
	}
	
	@Override
	public HeatingStatus getStatus(String code) {
		if (code == null) 
			return null;
		for (HeatingStatus status : HeatingStatus.values()) {
			if (code.equals(status.getCode())) {
				return status;
			}
		}	
		return null;
	}

}
