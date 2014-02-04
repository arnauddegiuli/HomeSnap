package com.adgsoftware.mydomo.engine.actuator.what;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.actuator.what.core.StateValue;

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

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		// TODO Auto-generated method stub
		
	}
}
