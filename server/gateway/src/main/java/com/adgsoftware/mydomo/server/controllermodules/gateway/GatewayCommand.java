package com.adgsoftware.mydomo.server.controllermodules.gateway;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway.GatewayDimension;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Date;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class GatewayCommand {
	
	// OSGi Shell Function	
	static final String[] functions = {
		"date", 			"datetime",
		"time"};

	/**
	 * Change the date of the gateway
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public String date(int day, int month, int year) {
		Calendar c = new GregorianCalendar(year, month, day);
		Date d = new Date();
		d.setTime(c.getTime());
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, "", GatewayDimension.DATE, formatDimension(d.getValueList())}));
	}
	
	/**
	 * Change the date and time of the gateway
	 * @param address
	 * @param sec
	 * @param min
	 * @param hour
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public String datetime(int sec, int min, int hour, int day, int month, int year) {

		Calendar c = new GregorianCalendar(year, month, day, hour, min, sec);
		Date d = new Date();
		d.setTime(c.getTime());

		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, "", GatewayDimension.DATETIME, formatDimension(d.getValueList())}));
	}

	/**
	 * Change the time of the gateway!
	 * @param address
	 * @param sec
	 * @param min
	 * @param hour
	 * @return
	 */
	public String time(String address, int sec, int min, int hour) {

		Calendar c = new GregorianCalendar();
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.HOUR_OF_DAY, sec);
		c.set(Calendar.DAY_OF_MONTH, sec);
		Date d = new Date();
		d.setTime(c.getTime());

		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, "", GatewayDimension.TIME, formatDimension(d.getValueList())}));
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
