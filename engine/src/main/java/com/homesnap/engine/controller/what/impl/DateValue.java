package com.homesnap.engine.controller.what.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.homesnap.engine.controller.what.StateValue;

public class DateValue implements StateValue {

	private String DATE_PATTERN = "dd-MM-yyyy";
	
	/** */
	private Date value;
	
	/**
	 * 
	 * @param value
	 */
	public DateValue(Date value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return new SimpleDateFormat(DATE_PATTERN).format(value);
	}

	public Date getDate() {
		return value;
	}
}
