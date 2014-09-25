package com.homesnap.engine.controller.types;

import com.homesnap.engine.controller.what.StateValueType;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class LabelType extends StateValueType {
	
	/** */
	private String value;

	@Override
	public String getValue() {
		return value;
	}

	@Override
	protected void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
