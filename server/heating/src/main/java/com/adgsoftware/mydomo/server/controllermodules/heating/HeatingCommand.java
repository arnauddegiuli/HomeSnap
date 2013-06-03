package com.adgsoftware.mydomo.server.controllermodules.heating;

import java.text.MessageFormat;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone.HeatingZoneDimension;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class HeatingCommand {
	
	// OSGi Shell Function	
	static final String[] functions = {
		"temperature", 			"mode",
		"time"};

	/**
	 * Change the temperature of the heating system
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public String temperature(double temperature) {
		DesiredTemperature dt = new DesiredTemperature();
		dt.setDesiredTemperature(temperature);
		dt.setMode(1);
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_HEATING_ADJUSTMENT, "", HeatingZoneDimension.SET_TEMPERATURE, formatDimension(dt.getValueList())}));
	}
	
	/**
	 * Change the mode of the heating system
	 * @param mode
	 * @return
	 */
	public String mode(int mode) {
		return "";
//		TODO return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, "", GatewayDimension.DATETIME, formatDimension(d.getValueList())}));
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
