package com.homesnap.engine.controller.temperature;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.state.StateName;
import com.homesnap.engine.controller.state.type.DoubleValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class TemperatureSensor extends Controller {
	
	/**
	 * 
	 */
	public TemperatureSensor(String address) {
		super(address);
	}
	
	/**
	 * 
	 * @return
	 */
	public DoubleValue getAlarmHigh() {
		return (DoubleValue) get(StateName.ALARM_HIGH); // TODO How to return primitive double if state is null ?
	}
	
	/**
	 * 
	 * @param alarm
	 */
	public void setAlarmHigh(double alarm) {
		set(StateName.ALARM_HIGH, new DoubleValue(alarm));
	}

	/**
	 * 
	 * @return
	 */
	public DoubleValue getAlarmLow() {
		return (DoubleValue) get(StateName.ALARM_LOW); // TODO How to return primitive double if state is null ?
	}
	
	/**
	 * 
	 * @param alarm
	 */
	public void setAlarmLow(double alarm) {
		set(StateName.ALARM_LOW, new DoubleValue(alarm));
	}
	
	/**
	 * 
	 * @return
	 */
	public DoubleValue getHighestTemperature() {
		return (DoubleValue) get(StateName.HIGHEST_TEMP); // TODO How to return primitive double if state is null ?
	}
	
	/**
	 * 
	 * @return
	 */
	public DoubleValue getLowestTemperature() {
		return (DoubleValue) get(StateName.LOWEST_TEMP); // TODO How to return primitive double if state is null ?
	}
	
	/**
	 * 
	 * @return
	 */
	public TemperatureUnit getUnit() {
		return (TemperatureUnit) get(StateName.UNIT);
	}
	
	/**
	 * 
	 * @param unit
	 */
	public void setUnit(TemperatureUnit unit) {
		set(StateName.UNIT, unit);
	}
	
	@Override
	protected void initStateTypes() {
		declareState(StateName.ALARM_HIGH, DoubleValue.class);
		declareState(StateName.ALARM_LOW, DoubleValue.class);
		declareState(StateName.HIGHEST_TEMP, DoubleValue.class);
		declareState(StateName.LOWEST_TEMP, DoubleValue.class);
		declareState(StateName.STATUS, TemperatureStatus.class);
		declareState(StateName.UNIT, TemperatureUnit.class);
	}
}
