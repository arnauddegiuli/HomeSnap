package com.homesnap.engine.controller.heating;

import java.util.List;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.impl.DoubleState;
import com.homesnap.engine.controller.who.Who;

public class HeatingZone extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	
	public enum HeatingZoneStatus implements State<HeatingZoneStatus> {
		HEATING_MODE,
		THERMAL_PROTECTION,
		OFF
		; 

		@Override
		public HeatingZoneStatus getValue() {
			return this;
		}

		@Override
		public void setValue(HeatingZoneStatus value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fromString(String value) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public enum ZoneStateName {
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
	}

	@Override
	public Who getWho() {
		return Who.HEATING_ADJUSTMENT;
	}

	public HeatingZoneStatus getStatus() {
		return (HeatingZoneStatus) get(ZoneStateName.STATUS.name());
	}

	public void setStatus(HeatingZoneStatus value) {
		set(ZoneStateName.STATUS.name(), value);
	}

	public DoubleState getHeatingDesiredTemperature() {
		return (DoubleState) get(ZoneStateName.SET_TEMPERATURE_HEATING.name());
	}

	public DoubleState getConditionalDesiredTemperature() {
		return (DoubleState) get(ZoneStateName.SET_TEMPERATURE_CONDITIONAL.name());
	}

	public DoubleState getGenericDesiredTemperature() {
		return (DoubleState) get(ZoneStateName.SET_TEMPERATURE_GENERIC.name());
	}

	public void setOffset(Offset offset) {
		set(ZoneStateName.LOCAL_OFFSET.name(), offset);
	}

	public Offset getOffset() {
		return (Offset) get(ZoneStateName.LOCAL_OFFSET.name());
	}

	public DoubleState getMeasureTemperature() {
		return (DoubleState) get(ZoneStateName.MEASURE_TEMPERATURE.name());
	}

	@Override
	public List<String> getStateList() {
		// TODO Auto-generated method stub
		return null;
	}
}
