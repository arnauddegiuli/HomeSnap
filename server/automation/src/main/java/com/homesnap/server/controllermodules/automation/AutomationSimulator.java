package com.homesnap.server.controllermodules.automation;

/*
 * #%L
 * MyDomoServerAutomationModule
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

import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;
import com.homesnap.engine.connector.openwebnet.OpenWebNetWho;
import com.homesnap.engine.connector.openwebnet.parser.CommandParser;
import com.homesnap.engine.connector.openwebnet.parser.ParseException;
import com.homesnap.engine.controller.automation.Automation;
import com.homesnap.server.controllermodules.ControllerSimulator;

public class AutomationSimulator implements ControllerSimulator {
	
	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	
	@Override
	public String execute(String command) {
		try {
			CommandParser parser = CommandParser.parse(command);
			String what = parser.getWhat();
			String where = parser.getWhere();
			if (Automation.AutomationState.AUTOMATION_DOWN.getValue().equals(what)
					|| Automation.AutomationState.AUTOMATION_STOP.getValue().equals(what)
					|| Automation.AutomationState.AUTOMATION_UP.getValue().equals(what)) {
				statusList.put(where, what);
				return OpenWebNetConstant.ACK;
			} else {
				System.out.println("Command not supported [" + command + "]");
				return OpenWebNetConstant.NACK;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String status(String command) {
		String where;
		try {
			where = CommandParser.parse(command).getWhere();
			String what = statusList.get(where);
			if (what == null) {
				what = Automation.AutomationState.AUTOMATION_STOP.getValue();
				statusList.put(where, what);
			}

			return MessageFormat.format(OpenWebNetConstant.COMMAND, new Object[] {OpenWebNetWho.WHO_AUTOMATION, what, where} ) + OpenWebNetConstant.ACK;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public String getWho() {
		return OpenWebNetWho.WHO_AUTOMATION;
	}
}