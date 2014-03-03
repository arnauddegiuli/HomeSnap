package com.homesnap.engine.controller.what;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum StateNameEnum implements StateName {
	
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
	
	UNIT(StateNames.UNIT)
	;
	
	private String name;
	
	StateNameEnum(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
