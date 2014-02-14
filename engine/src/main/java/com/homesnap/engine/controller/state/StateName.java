package com.homesnap.engine.controller.state;

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


/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum StateName implements StateNames {
	
	STATUS(StateNames.STATUS),
	
	LEVEL(StateNames.LEVEL),
	
	DATE(StateNames.DATE),
	TIME(StateNames.TIME),
	DATE_TIME(StateNames.DATE_TIME),
	
	ALARM(StateNames.ALARM),
	ALARM_HIGH(StateNames.ALARM_HIGH),
	ALARM_LOW(StateNames.ALARM_LOW),
	HIGHEST_TEMP(StateNames.HIGHEST_TEMP),
	LOWEST_TEMP(StateNames.LOWEST_TEMP),
	UNIT(StateNames.UNIT);
	
	private String name;
	private StateName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static StateName fromValue(String name) {
		for (StateName state : StateName.values()) {
			if (state.getName().equals(name)) {
				return state;
			}
		}
		return null;
	}
}
