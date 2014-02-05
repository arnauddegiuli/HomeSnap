package com.homesnap.engine.controller.state;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
// TODO If each controller declare is own StateName enum, maybe this class will be deleted
public interface StateNames {
	
	public static final String STATUS = "status";
	
	public static final String LEVEL = "level";
	
	public static final String DATE = "date";
	public static final String TIME = "time";
	public static final String DATE_TIME = "date-time";
	
	public static final String ALARM = "alarm";
	public static final String ALARM_HIGH = "alarm-high";
	public static final String ALARM_LOW = "alarm-low";
	public static final String HIGHEST_TEMP = "highest-temp";
	public static final String LOWEST_TEMP = "lowest-temp";
	public static final String UNIT = "unit";
}
