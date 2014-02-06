package com.adgsoftware.mydomo.engine.controller.what.impl;

import com.adgsoftware.mydomo.engine.controller.what.StateValue;

public enum OnOffValue implements StateValue {
	ON("on"),
	OFF("off");

	private String value;
	private OnOffValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
