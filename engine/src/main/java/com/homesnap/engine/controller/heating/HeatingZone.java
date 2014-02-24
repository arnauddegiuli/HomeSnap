package com.homesnap.engine.controller.heating;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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


import java.util.Map;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.impl.DoubleValue;
import com.homesnap.engine.controller.who.Who;

public class HeatingZone extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	
	public enum ZoneStatus implements StateValue {
		
		HEATING_GENERIC_HOLIDAY_DAILY("315"); 
		
		private String value;
		private ZoneStatus(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public enum HeatingStateName implements StateName {
		MEASURE_TEMPERATURE("0"), 
		FAN_COIL_SPEED("11"),
		PROBE_STATUS("12"),
		LOCAL_OFFSET("13"),
		SET_TEMPERATURE("14"),
		VALVE_STATUS("19"),
		ACTUATOR_STATUS("20");
		
		private String name;
		private HeatingStateName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

//		public static HeatingZoneDimension fromValue(String code) {
//			for (HeatingZoneDimension gd : HeatingZoneDimension.values()) {
//				if (gd.getCode().equals(code))
//					return gd;
//			}
//			
//			return null;
//		}
	}

	@Override
	public Who getWho() {
		return Who.HEATING_ADJUSTMENT;
	}
	
//	@Override TODO monter status au dessus?
	public ZoneStatus getStatus() {
		return (ZoneStatus) get(HeatingStateName.STATUS);
	}

	/**
	 * Asynchronously method to get value.
	 * @param mode
	 * @param callback the method to call when result will be available
	 */
	public DoubleValue getDesiredTemperature(final HeatingModeEnum mode) {
		// TODO manage heating mode
		return (DoubleValue) get(HeatingStateName.SET_TEMPERATURE);
	}
	
	/**
	 * Define the desired temperature. Value can be not be change instantly: it will send order
	 * to server. Only when monitor session will get the change, this will affect this controller.
	 * @param temperature
	 * @param mode
	 */
	public void setDesiredTemperature(DoubleValue temperature, HeatingModeEnum mode) {
		set(HeatingStateName.SET_TEMPERATURE, temperature);
	}

	/**
	 * A
	 */
	public DoubleValue getMeasureTemperature() {
		return (DoubleValue) get(HeatingStateName.MEASURE_TEMPERATURE);
	}

//	public ValvesStatus getValvesStatus() {
//		(ValvesStatus) get(HeatingStateName.VALVE_STATUS);
//	}
//	
//	public void getSetOffset(final DimensionStatusCallback<SetOffset> callback) {
//		getDimensionStatus(SetOffset.class, new DimensionStatusListener<SetOffset>() {
//			@Override
//			public void onDimensionStatus(SetOffset status,
//					CommandResult result) {
//				callback.value(status);
//			}
//		});
//	}

	@Override
	protected Map<StateName, Class<? extends StateValue>> getSupportedStateTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}