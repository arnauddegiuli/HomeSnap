package com.homesnap.engine.controller.heating;

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


import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.heating.stateValue.Offset;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.impl.DoubleValue;
import com.homesnap.engine.controller.who.Who;

public class HeatingZone extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	
	public enum HeatingZoneStatus implements StateValue {
		HEATING_MODE,
		THERMAL_PROTECTION,
		OFF
		; 

		@Override
		public String getValue() {
			return name();
		}
		
	}
	
	public enum ZoneStateName implements StateName {
		STATUS,
		MEASURE_TEMPERATURE, 
// TODO		FAN_COIL_SPEED,
// TODO		PROBE_STATUS,
		LOCAL_OFFSET,
		SET_TEMPERATURE_HEATING, 
		SET_TEMPERATURE_CONDITIONAL, 
		SET_TEMPERATURE_GENERIC;
// TODO		VALVE_STATUS,
// TODO		ACTUATOR_STATUS;
		
		public String getName() {
			return name();
		}
	}

	@Override
	public Who getWho() {
		return Who.HEATING_ADJUSTMENT;
	}

	public HeatingZoneStatus getStatus() {
		return (HeatingZoneStatus) get(ZoneStateName.STATUS);
	}

	public void setStatus(HeatingZoneStatus value) {
		set(ZoneStateName.STATUS, value);
	}

	public DoubleValue getHeatingDesiredTemperature() {
		return (DoubleValue) get(ZoneStateName.SET_TEMPERATURE_HEATING);
	}

	public DoubleValue getConditionalDesiredTemperature() {
		return (DoubleValue) get(ZoneStateName.SET_TEMPERATURE_CONDITIONAL);
	}

	public DoubleValue getGenericDesiredTemperature() {
		return (DoubleValue) get(ZoneStateName.SET_TEMPERATURE_GENERIC);
	}

	public void setOffset(Offset offset) {
		set(ZoneStateName.LOCAL_OFFSET, offset);
	}

	public Offset getOffset() {
		return (Offset) get(ZoneStateName.LOCAL_OFFSET);
	}

	public DoubleValue getMeasureTemperature() {
		return (DoubleValue) get(ZoneStateName.MEASURE_TEMPERATURE);
	}

	@Override
	protected StateName initStateName(String name) {
		return ZoneStateName.valueOf(name.toUpperCase());
	}

//	@Override
//	protected void initStateTypes() {
//		declareState(ZoneStateName.STATUS, HeatingZoneStatus.class);
//		declareState(ZoneStateName.ACTUATOR_STATUS, stateClass);
//		declareState(ZoneStateName.FAN_COIL_SPEED, );
//		declareState(ZoneStateName.LOCAL_OFFSET, Offset.class);
//		declareState(ZoneStateName.MEASURE_TEMPERATURE, DoubleValue.class);
//		declareState(ZoneStateName.PROBE_STATUS, );
//		declareState(ZoneStateName.SET_TEMPERATURE_CONDITIONAL, DoubleValue.class);
//		declareState(ZoneStateName.SET_TEMPERATURE_GENERIC, DoubleValue.class);
//		declareState(ZoneStateName.SET_TEMPERATURE_HEATING, DoubleValue.class);
//		declareState(ZoneStateName.VALVE_STATUS, );
//	}
}
