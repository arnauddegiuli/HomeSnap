package com.homesnap.engine.controller.what.impl;

import com.homesnap.engine.controller.what.StateValue;

public class PercentValue implements StateValue{

	public int value;
	
	public PercentValue(int value) {
		if (value < 0) {
			this.value = 0;
		} else if (value > 100) {
			this.value =100;
		} else {
			this.value = value;
		}
	}

	@Override
	public String getValue() {
		return String.valueOf(value);
	}

	public int getPercentValue() {
		return value;
	}
}
