package com.homesnap.engine.controller.temperature;

import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum TemperatureUnit implements StateValue {
	
	/** Indicates that the unit of measurement of the temperature is expressed in degrees Celsius */
	CELSIUS("C°"),
	
	/** Indicates that the unit of measurement of the temperature is expressed in degrees Fahrenheit */
	FAHRENHEIT("F°");
	
	private String value;
	
	private TemperatureUnit(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
}
