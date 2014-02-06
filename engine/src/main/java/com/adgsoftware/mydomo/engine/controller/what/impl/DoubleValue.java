package com.adgsoftware.mydomo.engine.controller.what.impl;

import com.adgsoftware.mydomo.engine.controller.what.StateValue;

public class DoubleValue implements StateValue {

	private double value;
	
	public DoubleValue(double value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return String.valueOf(value);
	}

	public double getDoubleValue() {
		return value;
	}

}
