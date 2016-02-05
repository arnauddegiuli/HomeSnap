package com.homesnap.engine.connector.openwebnet;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.connector.openwebnet.convert.OpenWebNetCommand;
import com.homesnap.engine.connector.openwebnet.convert.UnknownState;
import com.homesnap.engine.connector.openwebnet.convert.UnknownWho;
import com.homesnap.engine.connector.openwebnet.parser.ParseException;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
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
	private Map<String, State<?>> whatList;
	
	public OpenWebNetCommandResult(String commandResult, CommandResultStatus status, Map<String, State<?>> whatList) {
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
		this.whatList = whatList;
	}

	public String getResult() {
		return commandResult.toString();
	}

	public CommandResultStatus getStatus() {
		return status;
	}

	@Override
	public What getWhat(String name) {
		if (command != null) {
			List<What> what;
			try {
				if (OpenWebNetCommand.DEFAULT_ACTION.equalsIgnoreCase(name)) {
					what = command.getWhat(whatList);
				} else {		
					what = command.getDimension(whatList);
				}
				for (What w : what) {
					if (name == w.getName()) {
						return w;
					}
				}
			} catch (UnknownState e) {
				return null;
			} catch (UnknownWho e) {
				return null;
			}			
			
		}
		return null;
		
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
