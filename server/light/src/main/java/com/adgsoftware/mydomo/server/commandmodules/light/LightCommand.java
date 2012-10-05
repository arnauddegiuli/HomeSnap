package com.adgsoftware.mydomo.server.commandmodules.light;

import java.text.MessageFormat;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class LightCommand {
	
	// OSGi Shell Function
	static final String[] functions = { "on", "off" };
	
	public String on(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, Light.LightStatus.LIGHT_ON.getCode(), address} ));
	}
	
	public String off(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, Light.LightStatus.LIGHT_OFF.getCode(), address} ));
	}
}
