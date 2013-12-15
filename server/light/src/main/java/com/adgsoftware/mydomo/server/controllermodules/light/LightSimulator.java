package com.adgsoftware.mydomo.server.controllermodules.light;

/*
 * #%L
 * MyDomoServerLightModule
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.text.MessageFormat;
import java.util.Hashtable;

import com.adgsoftware.mydomo.engine.connector.openwebnet.Command;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.server.controllermodules.ControllerSimulator;

public class LightSimulator implements ControllerSimulator {
	
	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	
	@Override
	public String execute(String command) {
		try {
			Command parser = Command.getCommandAnalyser(command);
			String what = parser.getWhatFromCommand();
			String where = parser.getWhereFromCommand();
			if (Light.LightStatus.LIGHT_OFF.getCode().equals(what)
					|| Light.LightStatus.LIGHT_ON.getCode().equals(what)) {
				statusList.put(where, what);
				return Command.ACK;
			} else {
				System.out.println("Command not supported [" + command + "]");
				return Command.NACK;
			}
		} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
		}
	}
	
	@Override
	public String status(String command) {
		try {
			Command parser = Command.getCommandAnalyser(command);
			String where = parser.getWhereFromCommand();
			String what = statusList.get(where);
			if (what == null) {
				what = Light.LightStatus.LIGHT_OFF.getCode();
				statusList.put(where, what);
			}

			return MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, what, where} ) + Command.ACK;
		} catch (ParseException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getWho() {
		return Command.WHO_LIGHTING;
	}
}
