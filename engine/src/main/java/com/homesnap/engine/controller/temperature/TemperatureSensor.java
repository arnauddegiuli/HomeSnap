package com.homesnap.engine.controller.temperature;

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
