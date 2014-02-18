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
public class CommandResultImpl implements CommandResult {
	private String commandResult;
	private CommandResultStatus status;
	private Log log = new Log();
	private CommandConstant parser;
	
	public CommandResultImpl(String commandResult, CommandResultStatus status) {
		this.commandResult = commandResult;
		this.status = status;
		if (commandResult != null &&!CommandConstant.ACK.equals(commandResult) && !CommandConstant.NACK.equals(commandResult)) {
			try {
				parser = CommandConstant.getCommandAnalyser(commandResult);
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
	public State getWhat(StateName name) {
		if (parser != null) {
			
			if (StateName.STATUS.equals(name)) {
				return new State(StateName.STATUS, StatusMapping.convert(getWho(), parser.getWhatFromCommand())); // TODO revoir les getWho()
				// TODO manage different type.
			}
			return null;
			// TODO manage dimension
		} else {
			return null;
		}
	}

	@Override
	public Who getWho() {
		if (parser != null) {
			return OpenWebNetWho.convert(parser.getWhoFromCommand()); // TODO revoir ca!
		} else {
			return null;
		}
	}

	@Override
	public Where getWhere() {
		if (parser != null) {
			String where = parser.getWhereFromCommand();
			return new Where(where, where);
		} else {
			return null;
		}
	}

}
