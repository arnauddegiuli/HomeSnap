package com.adgsoftware.mydomo.server.controllermodules.automation;

import java.text.MessageFormat;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class AutomationCommand {
	
	// OSGi Shell Function
	static final String[] functions = { "up", "down", "stop" };
	
	public String up(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_AUTOMATION, Automation.AutomationStatus.AUTOMATION_UP.getCode(), address} ));
	}
	
	public String down(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_AUTOMATION, Automation.AutomationStatus.AUTOMATION_DOWN.getCode(), address} ));
	}
	
	public String stop(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_AUTOMATION, Automation.AutomationStatus.AUTOMATION_STOP.getCode(), address} ));	
	}
}
