package com.homesnap.engine.controller.types;

import com.homesnap.engine.controller.what.StateValueType;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class NumberType extends StateValueType {
	
	/** */
	private double value;
	
	/**
	 * 
	 */
	public NumberType() {
	}

	@Override
	public String getValue() {
		return String.valueOf(this.value);
	}

	@Override
	public void setValue(String value) {
		try {
			this.value = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Value "+ value +" is not a valid number.");
		}
	}

	@Override
	public String toString() {
		return getValue();
	}
}
