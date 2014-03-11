package com.homesnap.engine.controller.what.impl;

import java.util.Date;

import com.homesnap.engine.controller.what.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class DateValue implements StateValue {
	
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
		return String.valueOf(value);
	}
}
