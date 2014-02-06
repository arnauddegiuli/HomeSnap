package com.adgsoftware.mydomo.engine.oldconnector.openwebnet;

import java.util.List;
import java.util.logging.Level;

import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;
import com.adgsoftware.mydomo.engine.oldconnector.CommandResult;
import com.adgsoftware.mydomo.engine.oldconnector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.oldconnector.openwebnet.parser.ParseException;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionValue;

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
public class CommandResultImpl implements CommandResult {
	private String commandResult;
	private CommandResultStatus status;
	private Log log = new Log();
	private Command parser;
	
	public CommandResultImpl(String commandResult, CommandResultStatus status) {
		this.commandResult = commandResult;
		this.status = status;
		if (commandResult != null &&!Command.ACK.equals(commandResult) && !Command.NACK.equals(commandResult)) {
			try {
				parser = Command.getCommandAnalyser(commandResult);
			} catch (ParseException e) {
				log.log(Session.Command, Level.SEVERE, "Unknown command result [" + commandResult + "].");
			}
		}
	}

	public String getResult() {
		return commandResult;
	}

	public CommandResultStatus getStatus() {
		return status;
	}

	@Override
	public String getWhat() {
		if (parser != null) {
			return parser.getWhatFromCommand();
		} else {
			return null;
		}
	}

	@Override
	public String getWho() {
		if (parser != null) {
			return parser.getWhoFromCommand();
		} else {
			return null;
		}
	}

	@Override
	public String getWhere() {
		if (parser != null) {
			return parser.getWhereFromCommand();
		} else {
			return null;
		}
	}

	@Override
	public List<DimensionValue> getDimensionList() {
		if (parser != null) {
			return parser.getDimensionListFromCommand();
		} else {
			return null;
		}
	}
}
