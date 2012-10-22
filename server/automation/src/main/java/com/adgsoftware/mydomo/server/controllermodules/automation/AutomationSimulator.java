package com.adgsoftware.mydomo.server.controllermodules.automation;

import java.text.MessageFormat;
import java.util.Hashtable;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.server.controllermodules.ControllerSimulator;

public class AutomationSimulator implements ControllerSimulator {
	
	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	
	@Override
	public String execute(String command) {
		String what = Command.getWhatFromCommand(command);
		String where = Command.getWhereFromCommand(command);
		if (Automation.AutomationStatus.AUTOMATION_DOWN.getCode().equals(what)
				|| Automation.AutomationStatus.AUTOMATION_STOP.getCode().equals(what)
				|| Automation.AutomationStatus.AUTOMATION_UP.getCode().equals(what)) {
			statusList.put(where, what);
			return Command.ACK;
		} else {
			System.out.println("Command not supported [" + command + "]");
			return Command.NACK;
		}
	}
	
	@Override
	public String status(String command) {
		String where = Command.getWhereFromCommand(command);
		String what = statusList.get(where);
		if (what == null) {
			what = Automation.AutomationStatus.AUTOMATION_STOP.getCode();
			statusList.put(where, what);
		}

		return MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_AUTOMATION, what, where} ) + Command.ACK;
	}

	@Override
	public String getWho() {
		return Command.WHO_AUTOMATION;
	}
}
