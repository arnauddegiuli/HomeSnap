package com.homesnap.engine.connector.openwebnet.heating;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.heating.dimension.DesiredTemperature;
import com.homesnap.engine.connector.openwebnet.heating.dimension.MeasureTemperature;
import com.homesnap.engine.connector.openwebnet.heating.dimension.SetOffset;
import com.homesnap.engine.connector.openwebnet.heating.dimension.ValvesStatus;
import com.homesnap.engine.controller.heating.HeatingZone;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;

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


public enum HeatingZoneDimension {
	MEASURE_TEMPERATURE("0", HeatingZone.ZoneStateName.MEASURE_TEMPERATURE, MeasureTemperature.class), 
	FAN_COIL_SPEED("11", HeatingZone.ZoneStateName.MEASURE_TEMPERATURE, MeasureTemperature.class),
	PROBE_STATUS("12", HeatingZone.ZoneStateName.MEASURE_TEMPERATURE, MeasureTemperature.class),
	LOCAL_OFFSET("13", HeatingZone.ZoneStateName.LOCAL_OFFSET, SetOffset.class),
	SET_TEMPERATURE("14", HeatingZone.ZoneStateName.SET_TEMPERATURE_HEATING, DesiredTemperature.class),
	VALVE_STATUS("19", HeatingZone.ZoneStateName.MEASURE_TEMPERATURE, ValvesStatus.class),
	ACTUATOR_STATUS("20", HeatingZone.ZoneStateName.MEASURE_TEMPERATURE, MeasureTemperature.class);
	
	private String code;
	private StateName name;
	private Class<? extends DimensionStatus> clazz;
	
	private HeatingZoneDimension(String code, StateName name, Class<? extends DimensionStatus> clazz) {
		this.code = code;
		this.name = name;
		this.clazz = clazz;
	}
	
	public String getCode() {
		return code;
	}
	
	public static HeatingZoneDimension fromValue(String code) {
		for (HeatingZoneDimension gd : HeatingZoneDimension.values()) {
			if (gd.getCode().equals(code))
				return gd;
		}
		
		return null;
	}
	
	public static State fromDimensionValue(DimensionStatus value) {
		for (HeatingZoneDimension gd : HeatingZoneDimension.values()) {
			if (gd.getCode().equals(value.getCode())) {
				return new State(HeatingZoneDimension.fromValue(value.getCode()).getName(), null);
			}
		}
		return null;
	}
	
	public static DimensionStatus fromState(State value) {
		StateName name = value.getName();	
		if (name == null) {
			return null;
		}
		for (HeatingZoneDimension gd : HeatingZoneDimension.values()) {
			if (gd.getName().getName().equals(name.getName())) {
				DimensionStatus result = gd.createDimensionStatus();
				result.setStateValue(value.getValue());
			}
		}
		return null;
	}

	public DimensionStatus createDimensionStatus() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public StateName getName() {
		return name;
	}

	
	
}
