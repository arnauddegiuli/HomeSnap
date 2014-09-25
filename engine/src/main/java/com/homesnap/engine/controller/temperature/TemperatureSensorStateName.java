package com.homesnap.engine.controller.temperature;

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
