package com.adgsoftware.mydomo.engine.controller.heating;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.heating.Heating.HeatingStatus;

public class HeatingDimension extends Controller<HeatingStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
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
