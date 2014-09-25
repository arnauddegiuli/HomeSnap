package com.homesnap.engine.controller.types;

import com.homesnap.engine.controller.what.StateValueType;

/**
 * Class representing an interval of numeric values defined by a minimum and a maximum value.
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 * @param <T>
 */
public abstract class MinMaxType<T extends Number> extends StateValueType {
	
	/** */
	private T minValue;
	
	/** */
	private T maxValue;
	
	/**
	 * 
	 * @param minValue
	 * @param maxValue
	 */
	MinMaxType(T minValue, T maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getMinValue() {
		return minValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getMaxValue() {
		return maxValue;
	}
	
	/**
	 * Throws an {@link IllegalArgumentException} explaning that the value must be between values defined by {@link #getMinValue()} and {@link #getMaxValue()} methods.
	 * @throws IllegalArgumentException if value is lower than the min value or greater than the max value of this type
	 */
	protected void throwInvalidValueException(String value) {
		throw new IllegalArgumentException("Invalid value "+ value +", must be between "+ getMinValue().toString() +" and "+ getMaxValue().toString() +".");
	}

	@Override
	public String toString() {
		return "["+ getMinValue().toString() +".."+ getMaxValue().toString() +"]";
	}
}