package com.homesnap.engine.controller.what.impl;


/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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

public class PercentageState extends MinMaxState<Integer> {
	
	public static final int MIN = 0;
	public static final int MAX = 100;
	
	private int value;
	
	/**
	 * 
	 */
	public PercentageState() {
		super(MIN, MAX);
	}
	
	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		try {
			if (MIN <= value && value <= MAX) {
				this.value = value;
			} else {
				throwInvalidValueException(String.valueOf(value));
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Value "+ value +" is not a valid number.");
		}
	}

	@Override
	public String toString() {
		return String.valueOf(value) +" %";
	}

	@Override
	public void fromString(String value) {
		Integer.parseInt(value.substring(0, value.length()-2)); // remove the " %"
		
	}
}
