package com.homesnap.engine.connector;

import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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
	public What getWhat(String name) {
		// TODO return a what empty with just the name!
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
