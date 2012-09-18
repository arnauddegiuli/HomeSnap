package com.adgsoftware.mydomo.test.controller.commandmodules;

import java.text.MessageFormat;
import java.util.Hashtable;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.light.Light;

public class LightCommand {

	public final static String execute(String command, Hashtable<String, String> statusList) {
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
	
	public final static String status(String command, Hashtable<String, String> statusList) {
		String where = Command.getWhereFromCommand(command);
		String what = statusList.get(where);
		if (what == null) {
			what = Light.LightStatus.LIGHT_OFF.getCode();
			statusList.put(where, what);
		}

		return MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, what, where} ) + Command.ACK;

	}
}
