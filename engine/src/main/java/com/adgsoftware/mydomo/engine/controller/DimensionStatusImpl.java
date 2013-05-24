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
	
	protected String getStringValue(int pos) {
		return dimensionList.get(pos).getValue();
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
		Integer unsignedByte = Integer.parseInt(dimensionList.get(pos).getValue());
		return unsignedByte.byteValue();
	}
	
	protected void setByteValue(byte value, int pos, int padding) {
		int unsignedByte = value & 0xff;
		String val = Integer.toString(unsignedByte);
		for(int i = val.length();i<padding;i++) {
			val = "0" + val;
		}
		dimensionList.get(pos).setValue(val);
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
	
	/**
	 * Format temperature with My Home SCS system
	 * @param value temperature in degree (example: 27,5°C = 27.5)
	 * @param pos
	 * @param padding
	 */
	protected void setTemperatureValue(Double value, int pos) {
		// TODO Check the temperature (more than 5 and less than 40)
		int padding = 4;
		String val = Integer.toString(new Double (value * 10.0).intValue());
		for(int i = val.length();i<padding;i++) {
			val = "0" + val;
		}
		dimensionList.get(pos).setValue(val);
	}
	
	protected Double getTemperatureValue(int pos) {
		double val = Double.parseDouble(dimensionList.get(pos).getValue());
		return new Double(val/10);
	}
}
