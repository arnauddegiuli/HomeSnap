package com.adgsoftware.mydomo.engine.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public abstract class DimensionStatusImpl implements DimensionStatus {

	private List<DimensionValue> dimensionList;
	private String what;
	
	public DimensionStatusImpl(List<DimensionValue> dimensionList, String what) {
		this.dimensionList = dimensionList;
		this.what = what;
	}
	
	public DimensionStatusImpl(DimensionValue[] dimensionArray, String what) {
		dimensionList = new ArrayList<DimensionValue>();
		dimensionList.addAll(Arrays.asList(dimensionArray));
		this.what = what;
	}
	
	@Override
	public List<DimensionValue> getValueList() {
		return dimensionList;
	}

	@Override
	public void setValueList(List<DimensionValue> dimensionList) {
		this.dimensionList = dimensionList;
	}
	
	@Override
	public String getCode() {
		return what;
	}
	
	protected int getIntValue(int pos) {
		return Integer.parseInt(dimensionList.get(pos).getValue());
	}
	
	protected void setIntValue(int value, int pos, int padding) {
		String val = Integer.toString(value);
		for(int i = val.length();i<padding;i++) {
			val = "0" + val;
		}
		dimensionList.get(pos).setValue(val);
	}
	
	protected byte getByteValue(int pos) {
		return Byte.parseByte(dimensionList.get(pos).getValue());
	}
	
	protected void setByteValue(byte value, int pos, int padding) {
		String val = Byte.toString(value);
		for(int i = val.length();i<padding;i++) {
			val = "0" + val;
		}
		dimensionList.get(pos).setValue(val); // TODO test it
	}
	
	protected TimeZone getTimeZoneValue(int pos) {
		String value = dimensionList.get(pos).getValue();
		String id;
		if ("0".equals(value.charAt(0))) {
			id = "GMT+" + value.substring(1)+":00";
		} else {
			id = "GMT-"  + value.substring(1)+":00";
		}
		
		
		return TimeZone.getTimeZone(id);
	}
	
	protected void setTimeZoneValue(TimeZone value, int pos) {
		int hours = value.getRawOffset()/1000/60/60;
		String val;
		if (hours>0) {
			if (hours < 10) {
				val = "00" + hours;
			} else {
				val = "0" + hours;
			}
			
		} else {
			if (hours < 10) {
				val = "10" + Math.abs(hours);
			} else {
				val = "1" + Math.abs(hours);
			}
		}
		dimensionList.get(pos).setValue(val);
	}
}
