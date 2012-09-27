package com.adgsoftware.mydomo.server.commandmodules.light;

import java.text.MessageFormat;
import java.util.Hashtable;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.server.commandmodules.ControllerCommand;

public class LightCommand implements ControllerCommand {

	@Override
	public String execute(String command, Hashtable<String, String> statusList) {
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
	public String status(String command, Hashtable<String, String> statusList) {
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
