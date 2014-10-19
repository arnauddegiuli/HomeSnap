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
	public void setValue(String value) {
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
