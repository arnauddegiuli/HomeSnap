package com.homesnap.engine.controller.types;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
