package com.adgsoftware.mydomo.server.controllermodules.heating;

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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.connector.openwebnet.OpenWebNetConstant;
import com.adgsoftware.mydomo.engine.connector.openwebnet.OpenWebNetWho;
import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionValue;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.Offset;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.Offset.Mode;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.ValveStatusEnum;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.dimension.MeasureTemperature;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.dimension.ProbeStatus;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.dimension.SetOffset;
import com.adgsoftware.mydomo.engine.connector.openwebnet.heating.dimension.ValvesStatus;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.CommandParser;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;
import com.adgsoftware.mydomo.engine.controller.Command;
import com.adgsoftware.mydomo.server.controllermodules.ControllerDimensionSimulator;

public class HeatingSimulator implements ControllerDimensionSimulator {

	
	private static Hashtable<String, List<DimensionValue>> dimensionCache = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	@Override
	public String execute(String command) {
		try {
			CommandParser parser = CommandParser.parse(command);
			String dimensionStr = parser.getDimension();
			String where = parser.getWhere();
			if (HeatingZoneDimension.SET_TEMPERATURE.getCode().equals(dimensionStr) ||
				HeatingZoneDimension.LOCAL_OFFSET.getCode().equals(dimensionStr) ||
				HeatingZoneDimension.MEASURE_TEMPERATURE.getCode().equals(dimensionStr) ||
				HeatingZoneDimension.PROBE_STATUS.getCode().equals(dimensionStr) ||
				HeatingZoneDimension.VALVE_STATUS.getCode().equals(dimensionStr)) {
				
				dimensionCache.put(where
						+ "-" + dimensionStr, parser.getDimensionList());
				return OpenWebNetConstant.ACK;
			} else {
				return OpenWebNetConstant.NACK;
			}
		} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
		}
	}

	@Override
	public String status(String command) {
		try {
			CommandParser parser = CommandParser.parse(command);
			String where = parser.getWhere();
			String dimensionStr = parser.getDimension();
			List<DimensionValue> dimensionList;
			if (HeatingZoneDimension.SET_TEMPERATURE.getCode().equals(dimensionStr)) {
				
				dimensionList = dimensionCache.get(where
						+ "-" + dimensionStr);
				if (dimensionList == null) {
					DesiredTemperature dt = new DesiredTemperature();
					dt.setDesiredTemperature(21d);
					dt.setMode(3); // Generic mode
					dimensionList = dt.getValueList();
					dimensionCache.put(where + "-" + dimensionStr, dimensionList);
				}
			} else if (HeatingZoneDimension.LOCAL_OFFSET.getCode().equals(dimensionStr)) {
				dimensionList = dimensionCache.get(where
						+ "-" + dimensionStr);
				if (dimensionList == null) {
					SetOffset so = new SetOffset();
					so.setLocalOffset(new Offset(Mode.OFF, 0));
					dimensionList = so.getValueList();
					dimensionCache.put(where + "-" + dimensionStr, dimensionList);
				}
			} else if (HeatingZoneDimension.MEASURE_TEMPERATURE.getCode().equals(
					dimensionStr)) {
				MeasureTemperature mt = new MeasureTemperature();
				mt.setMeasuredTemperature(18d);
				dimensionList = mt.getValueList();
			} else if (HeatingZoneDimension.PROBE_STATUS.getCode().equals(
					dimensionStr)) {
				// TODO
				ProbeStatus ps = new ProbeStatus();
				dimensionList = null;
			} else if (HeatingZoneDimension.VALVE_STATUS.getCode().equals(
					dimensionStr)) {
				ValvesStatus vs = new ValvesStatus();
				vs.setHeatingValveStatus(ValveStatusEnum.OFF);
				vs.setConditioningValveStatus(ValveStatusEnum.OFF);
				dimensionList = vs.getValueList();
			}  else {
				return OpenWebNetConstant.NACK;
			}
	
			if (dimensionList == null) {
				dimensionList = new ArrayList<DimensionValue>();
				dimensionCache.put(where + "-"
						+ dimensionStr, dimensionList);
			}
			StringBuilder sb = new StringBuilder();
			for (DimensionValue dimension : dimensionList) {
				sb.append(dimension.getValue());
				sb.append(OpenWebNetConstant.DIMENSION_SEPARATOR);
			}
			sb.setLength(sb.length() - 1);
			return MessageFormat.format(OpenWebNetConstant.DIMENSION_COMMAND, new Object[] {
					OpenWebNetWho.WHO_HEATING_ADJUSTMENT, where, dimensionStr, sb.toString() })
					+ OpenWebNetConstant.ACK;
		} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
		}
	}

	@Override
	public String getWho() {
		return OpenWebNetWho.WHO_HEATING_ADJUSTMENT;
	}	
}
