package com.homesnap.server.controllermodules.heating;

/*
 * #%L
 * MyDomoServerHeatingModule
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


import java.text.MessageFormat;
import java.util.List;

import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;
import com.homesnap.engine.connector.openwebnet.convert.OpenWebNetWho;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.homesnap.engine.connector.openwebnet.heating.dimension.DesiredTemperature;
import com.homesnap.engine.connector.openwebnet.heating.dimension.MeasureTemperature;
import com.homesnap.server.ControllerStateManagement;

public class HeatingCommand {
	
	// OSGi Shell Function	
	static final String[] functions = {
		"desiredTemperature", "currentTemperature"};

	/**
	 * Change the temperature of the heating system
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public String desiredTemperature(double temperature, String address) {
		DesiredTemperature dt = new DesiredTemperature();
		dt.setDesiredTemperature(temperature);
		dt.setMode(1);
		return ControllerStateManagement.executeCommand(MessageFormat.format(OpenWebNetConstant.DIMENSION_COMMAND, new Object[] {OpenWebNetWho.WHO_HEATING_ADJUSTMENT, address, HeatingZoneDimension.SET_TEMPERATURE, formatDimension(dt.getValueList())}));
	}
	
	/**
	 * Simulate the current temperature of a zone
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public String currentTemperature(double temperature, String address) {
		MeasureTemperature dt = new MeasureTemperature();
		dt.setMeasuredTemperature(temperature);
		return ControllerStateManagement.executeCommand(MessageFormat.format(OpenWebNetConstant.DIMENSION_COMMAND, new Object[] {OpenWebNetWho.WHO_HEATING_ADJUSTMENT, address, HeatingZoneDimension.MEASURE_TEMPERATURE.getCode(), formatDimension(dt.getValueList())}));
	}
	
	private String formatDimension(List<DimensionValue> valueList) {
		StringBuilder sb = new StringBuilder();
		for (DimensionValue value : valueList) {
			sb.append(value.getValue());
			sb.append(OpenWebNetConstant.DIMENSION_SEPARATOR);
		}
		
		if (sb.length() > 0) {
			sb.setLength(sb.length()-1);
		}
		
		return sb.toString();
	}
}
