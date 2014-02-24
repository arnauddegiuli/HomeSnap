package com.homesnap.engine.connector.openwebnet.heating;

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


public enum HeatingZoneDimension {
	MEASURE_TEMPERATURE("0"), 
	FAN_COIL_SPEED("11"),
	PROBE_STATUS("12"),
	LOCAL_OFFSET("13"),
	SET_TEMPERATURE("14"),
	VALVE_STATUS("19"),
	ACTUATOR_STATUS("20");
	
	private String code;
	private HeatingZoneDimension(String code) {
		this.code = code;
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
}
