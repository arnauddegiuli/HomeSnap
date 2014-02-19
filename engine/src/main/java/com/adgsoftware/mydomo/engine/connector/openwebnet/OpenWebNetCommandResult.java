package com.adgsoftware.mydomo.engine.connector.openwebnet;

import java.util.logging.Level;

import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;
import com.adgsoftware.mydomo.engine.controller.what.State;
import com.adgsoftware.mydomo.engine.controller.what.StateName;
import com.adgsoftware.mydomo.engine.controller.where.Where;
import com.adgsoftware.mydomo.engine.controller.who.Who;

/*
 * #%L
 * MyDomoEngine
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


/**
 * CommandResult is the result of a command sent to gateway.
 */
public class OpenWebNetCommandResult implements CommandResult {
	private String commandResult;
	private OpenWebNetCommand command;
	private CommandResultStatus status;
	private Log log = new Log();
	
	public OpenWebNetCommandResult(String commandResult, CommandResultStatus status) {
		this.commandResult = commandResult;
		if (! (OpenWebNetConstant.ACK.equals(commandResult) ||
				OpenWebNetConstant.NACK.equals(commandResult))) {
			try {
				command = new OpenWebNetCommand(commandResult);
			} catch (ParseException e) {
				log.log(Session.Command, Level.WARNING, "Unknown message received [" + commandResult +"]. Message dropped.");
			}
		}
		this.status = status;
	}

	public String getResult() {
		return commandResult.toString();
	}

	public CommandResultStatus getStatus() {
		return status;
	}

	@Override
	public State getWhat(StateName name) {
		if (command != null) {
			
			if (StateName.STATUS.equals(name)) {
				return command.getWhat();
			} else {
				return command.getDimension();
			}
		} else {
			return null;
		}
	}

	@Override
	public Who getWho() {
		if (command != null) {
			return command.getWho();
		} else {
			return null;
		}
	}

	@Override
	public Where getWhere() {
		if (command != null) {
			return command.getWhere();
		} else {
			return null;
		}
	}
}
