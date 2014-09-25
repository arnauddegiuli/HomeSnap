package com.homesnap.engine.controller.types;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class PercentageType extends MinMaxType<Integer> {
	
	public static final int MIN = 0;
	public static final int MAX = 100;
	
	private int value;
	
	/**
	 * 
	 */
	public PercentageType() {
		super(MIN, MAX);
	}
	
	@Override
	public String getValue() {
		return String.valueOf(this.value);
	}

	@Override
	protected void setValue(String value) {
		try {
			int number = Integer.parseInt(value);
			if (MIN <= this.value && this.value <= MAX) {
				this.value = number;
			} else {
				throwInvalidValueException(value);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Value "+ value +" is not a valid number.");
		}
	}

	@Override
	public String toString() {
		return super.toString() +" %";
	}
}