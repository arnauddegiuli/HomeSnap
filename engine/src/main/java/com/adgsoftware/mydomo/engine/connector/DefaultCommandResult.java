package com.adgsoftware.mydomo.engine.connector;

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

public class DefaultCommandResult implements CommandResult {
	private String commandResult;
	private CommandResultStatus status;
	
	public DefaultCommandResult(String commandResult, CommandResultStatus status) {
		this.commandResult = commandResult;
		this.status = status;
	}

	@Override
	public CommandResultStatus getStatus() {
		return status;
	}

	public String getResult() {
		return commandResult;
	}

	@Override
	public State getWhat(StateName name) {
		return null;
	}

	@Override
	public Who getWho() {
		return null;
	}

	@Override
	public Where getWhere() {
		return null;
	}
}
