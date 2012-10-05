package com.adgsoftware.mydomo.server.controllermodules.light;

import java.text.MessageFormat;
import java.util.Hashtable;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.server.controllermodules.ControllerSimulator;

public class LightSimulator implements ControllerSimulator {
	
	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	
	@Override
	public String execute(String command) {
		String what = Command.getWhatFromCommand(command);
		String where = Command.getWhereFromCommand(command);
		if (Light.LightStatus.LIGHT_OFF.getCode().equals(what)
				|| Light.LightStatus.LIGHT_ON.getCode().equals(what)) {
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
			what = Light.LightStatus.LIGHT_OFF.getCode();
			statusList.put(where, what);
		}

		return MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, what, where} ) + Command.ACK;

	}

	@Override
	public String getWho() {
		return Command.WHO_LIGHTING;
	}
}
