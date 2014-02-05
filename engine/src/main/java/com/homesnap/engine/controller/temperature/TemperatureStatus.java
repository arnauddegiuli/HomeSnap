package com.homesnap.engine.controller.temperature;

import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum TemperatureStatus implements StateValue {
	
	/** Indicates that the sensor is currently active */
	ACTIVE("active"),
	
	/** Indicates that the sensor is currently inactive */
	INACTIVE("inactive");
	
	/** The value of the status */
	private String value;
	
	private TemperatureStatus(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
}
