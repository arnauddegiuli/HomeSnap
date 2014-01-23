package com.adgsoftware.mydomo.engine;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

public interface JsonSerializable {

	public JSONObject toJson();
	public void fromJson(JSONObject jsonObject) throws UnmarshalException;

}
