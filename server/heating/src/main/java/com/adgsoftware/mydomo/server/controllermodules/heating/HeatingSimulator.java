package com.adgsoftware.mydomo.server.controllermodules.heating;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.heating.ValveStatusEnum;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.MeasureTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.ProbeStatus;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.SetOffset;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.ValvesStatus;
import com.adgsoftware.mydomo.server.controllermodules.ControllerDimensionSimulator;

public class HeatingSimulator implements ControllerDimensionSimulator {

	
	private static final String HEATING_ADDRESS = "4";
	private static Hashtable<String, List<DimensionValue>> dimensionCache = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	@Override
	public String execute(String command) {
		DimensionValue what = Command.getDimensionFromCommand(command);
		String where = Command.getWhereFromCommand(command);
		String dimensionStr = Command.getDimensionFromCommand(command).getValue();
		if (HeatingZone.HeatingZoneDimension.SET_TEMPERATURE.getCode().equals(what.getValue()) ||
			HeatingZone.HeatingZoneDimension.LOCAL_OFFSET.getCode().equals(what.getValue()) ||
			HeatingZone.HeatingZoneDimension.MEASURE_TEMPERATURE.getCode().equals(what.getValue()) ||
			HeatingZone.HeatingZoneDimension.PROBE_STATUS.getCode().equals(what.getValue()) ||
			HeatingZone.HeatingZoneDimension.VALVE_STATUS.getCode().equals(what.getValue())) {
			
			dimensionCache.put(where
					+ "-" + dimensionStr, Command.getDimensionListFromCommand(command));
			return Command.ACK;
		} else {
			return Command.NACK;
		}
	}

	@Override
	public String status(String command) {

		String where = HEATING_ADDRESS;
		String dimensionStr = Command.getDimensionFromCommand(command)
				.getValue();
		List<DimensionValue> dimensionList;
		if (HeatingZone.HeatingZoneDimension.SET_TEMPERATURE.getCode().equals(dimensionStr)) {
			
			dimensionList = dimensionCache.get(where
					+ "-" + dimensionStr);
			if (dimensionList == null) {
				DesiredTemperature dt = new DesiredTemperature();
				dt.setDesiredTemperature(21d);
				dt.setMode(3); // Generic mode
				dimensionList = dt.getValueList();
				dimensionCache.put(where + "-" + dimensionStr, dimensionList);
			}
		} else if (HeatingZone.HeatingZoneDimension.LOCAL_OFFSET.getCode().equals(dimensionStr)) {
			SetOffset so = new SetOffset();
			// TODO
			dimensionList = null;
		} else if (HeatingZone.HeatingZoneDimension.MEASURE_TEMPERATURE.getCode().equals(
				dimensionStr)) {
			MeasureTemperature mt = new MeasureTemperature();
			mt.setMeasuredTemperature(21d);
			dimensionList = mt.getValueList();
		} else if (HeatingZone.HeatingZoneDimension.PROBE_STATUS.getCode().equals(
				dimensionStr)) {
			// TODO
			ProbeStatus ps = new ProbeStatus();
			dimensionList = null;
		} else if (HeatingZone.HeatingZoneDimension.VALVE_STATUS.getCode().equals(
				dimensionStr)) {
			ValvesStatus vs = new ValvesStatus();
			vs.setHeatingValveStatus(ValveStatusEnum.OFF);
			dimensionList = vs.getValueList();
		}  else {
			return Command.NACK;
		}

		if (dimensionList == null) {
			dimensionList = new ArrayList<DimensionValue>();
			dimensionCache.put(where + "-"
					+ dimensionStr, dimensionList);
		}
		StringBuilder sb = new StringBuilder();
		for (DimensionValue dimension : dimensionList) {
			sb.append(dimension.getValue());
			sb.append(Command.DIMENSION_SEPARATOR);
		}
		sb.setLength(sb.length() - 1);
		return MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {
				Command.WHO_HEATING_ADJUSTMENT, where, dimensionStr, sb.toString() })
				+ Command.ACK;
	}

	@Override
	public String getWho() {
		return Command.WHO_HEATING_ADJUSTMENT;
	}	
}
