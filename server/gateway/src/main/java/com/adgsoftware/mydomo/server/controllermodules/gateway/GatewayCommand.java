package com.adgsoftware.mydomo.server.controllermodules.gateway;

import java.text.MessageFormat;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway.GatewayDimension;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class GatewayCommand {
	
	// OSGi Shell Function	
	static final String[] functions = {
		"date"/*, 			"datetime", 	"distribution_Version", 
		"firmware_Version", "ip_Address", 	"kernel_Version",
		"mac_Address", 		"model", 		"netmwask",
		"time", 			"uptime"*/};

	public String date(String address, int day, int month, int year) {
		String g = "" + day + Command.DIMENSION_SEPARATOR + month + Command.DIMENSION_SEPARATOR + year;
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {Command.WHO_GATEWAY, address, GatewayDimension.DATE, g}));
	}
}
