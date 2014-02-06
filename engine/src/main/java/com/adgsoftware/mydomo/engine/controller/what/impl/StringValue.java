package com.adgsoftware.mydomo.engine.controller.what.impl;

import com.adgsoftware.mydomo.engine.controller.what.StateValue;

public class StringValue implements StateValue {

	private String value;
	
	public StringValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
