package com.adgsoftware.mydomo.engine.actuator.what;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.actuator.what.core.StateValue;

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
