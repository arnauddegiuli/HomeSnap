package com.homesnap.engine.controller.light;

import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum LightStatus implements StateValue {
	
	/**
	 * Indicates that the light is on
	 */
	ON("on"),
	
	/**
	 * Indicates that the light is off
	 */
	OFF("off"),
	
	/**
	 * Indicates that the light is blinking
	 */
	BLINKING("blinking");
	
	private String value;
	
	private LightStatus(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
}
