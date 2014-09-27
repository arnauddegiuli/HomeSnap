package com.homesnap.engine.controller.types;


/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ListOfNumbersType extends ListOfValuesType {

	/**
	 * 
	 * @param values
	 */
	public ListOfNumbersType(String[] values) {
		super(values);
	}

	@Override
	public void setValue(String value) {
		try {
			Integer.parseInt(value);
			super.setValue(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Value "+ value +" is not a valid number.");
		}
	}
}
