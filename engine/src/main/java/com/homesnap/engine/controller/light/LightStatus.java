package com.homesnap.engine.controller.light;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum LightStatus implements StateValue {
	
	/**
	 * Indicates that the light is on
	 */
	ON("on"),
	
	/**
	 * Indicates that the light is off
	 */
	OFF("off"),
	
	/**
	 * Indicates that the light is blinking
	 */
	BLINKING("blinking");
	
	private String value;
	
	private LightStatus(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static LightStatus fromValue(String value) {
		for (LightStatus status : LightStatus.values()) {
			if (status.getValue().equals(value)) {
				return status;
			}
		}
		return null;
	}
}
