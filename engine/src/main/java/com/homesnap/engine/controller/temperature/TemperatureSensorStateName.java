package com.homesnap.engine.controller.temperature;

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


import com.homesnap.engine.controller.what.StateName;

/**
 * 	
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum TemperatureSensorStateName implements StateName {
	
	/** Defines the maximum temperature value beyond which the sensor is alarming */
	ALARM_HIGH,
	
	/** Defines the minumum temperature value beyond which the sensor is alarming */
	ALARM_LOW,
	
	/** Contains the highest temperature measured by the sensor */
	HIGHEST_TEMP,
	
	/** Contains the lowest temperature measured by the sensor */
	LOWEST_TEMP,
	
	/** Defines the precision of the values read on the sensor (HIGHEST_TEMP, LOWEST_TEMP and VALUE) */
	PRECISION,
	
	/** Contains the current temperature value */
	VALUE,
	
	/** Defines the unit of temperature of the values read on this sensor */
	UNIT;
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
}
