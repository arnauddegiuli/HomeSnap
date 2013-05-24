package com.adgsoftware.mydomo.server.controllermodules.heating;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway.GatewayDimension;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Date;
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
		Calendar c = new GregorianCalendar(1, 2, 3);
		Date d = new Date();
		d.setTime(c.getTime());
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, "", GatewayDimension.DATE, formatDimension(d.getValueList())}));
	}
	
	/**
	 * Change the mode of the heating system
	 * @param mode
	 * @return
	 */
	public String mode(int mode) {

		Calendar c = new GregorianCalendar(1, 2, 3, 4, 5, 6);
		Date d = new Date();
		d.setTime(c.getTime());

		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, "", GatewayDimension.DATETIME, formatDimension(d.getValueList())}));
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
