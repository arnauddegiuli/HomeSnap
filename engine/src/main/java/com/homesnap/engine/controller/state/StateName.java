package com.homesnap.engine.controller.state;

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
}
