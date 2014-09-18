package com.homesnap.engine.controller.types;

import com.homesnap.engine.controller.what.StateValueType;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ListOfValuesType extends StateValueType {
	
	/** */
	private String[] values;
	
	/** */
	private String value;
	
	/**
	 * 
	 * @param values
	 */
	public ListOfValuesType(String[] values) {
		this.values = new String[values.length];
		int count = 0;
		for (String s : values) {
			this.values[count++] = s.trim();
		}
	}
	
	/**
	 * @return the values
	 */
	public String[] getValues() {
		return values;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	protected void setValue(String value) {
		for (String s : values) {
			if (s.equals(value)) {
				this.value = value;
				return;
			}
		}
		throw new IllegalArgumentException("Value "+ value +" is not supported in this list of values.");
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		for (String s : values) {
			result.append(s).append(", ");
		}
		result.setLength(result.length() - 2);
		return result.append("]").toString();
	}
}
