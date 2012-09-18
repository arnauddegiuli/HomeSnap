package com.adgsoftware.mydomo.engine.controller;

public class DimensionValueImpl implements DimensionValue {

	String value;
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

}
