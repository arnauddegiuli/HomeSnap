package com.adgsoftware.mydomo.server.controllermodules.heating;

import java.text.MessageFormat;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone.HeatingZoneDimension;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.MeasureTemperature;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

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
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_HEATING_ADJUSTMENT, address, HeatingZoneDimension.SET_TEMPERATURE, formatDimension(dt.getValueList())}));
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
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_HEATING_ADJUSTMENT, address, HeatingZoneDimension.MEASURE_TEMPERATURE, formatDimension(dt.getValueList())}));
	}
	
	private String formatDimension(List<DimensionValue> valueList) {
		StringBuilder sb = new StringBuilder();
		for (DimensionValue value : valueList) {
			sb.append(value.getValue());
			sb.append(Command.DIMENSION_SEPARATOR);
		}
		
		if (sb.length() > 0) {
			sb.setLength(sb.length()-1);
		}
		
		return sb.toString();
	}
}
