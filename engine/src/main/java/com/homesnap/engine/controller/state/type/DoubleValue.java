package com.homesnap.engine.controller.state.type;

import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class DoubleValue implements StateValue {

	/** */
	private double value;
	
	/**
	 * 
	 * @param value
	 */
	public DoubleValue(double value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return String.valueOf(value);
	}

	/**
	 * 
	 * @return
	 */
	public double getDoubleValue() {
		return value;
	}
}
