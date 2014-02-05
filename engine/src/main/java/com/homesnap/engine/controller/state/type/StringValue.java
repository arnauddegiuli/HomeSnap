package com.homesnap.engine.controller.state.type;

import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StringValue implements StateValue {

	/** */
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	public StringValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}
}
