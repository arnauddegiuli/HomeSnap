package com.homesnap.engine.connector.openwebnet.convert;

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

import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.openwebnet.CommandEnum;
import com.homesnap.engine.connector.openwebnet.WhereType;
import com.homesnap.engine.connector.openwebnet.parser.CommandParser;
import com.homesnap.engine.connector.openwebnet.parser.ParseException;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

public class OpenWebNetCommand {

	private String command;
	private CommandParser parser;

	public OpenWebNetCommand(String command) throws ParseException {
		try {
			this.command = command;
			parser = CommandParser.parse(command);
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			throw e;
		}
	}

	public OpenWebNetCommand(Command command) {
		this.command = Convert.createMessage(command);
	}

	public boolean isStandardCommand() {
		return CommandEnum.STANDARD_COMMAND.equals(parser.getType());
	}

	public boolean isDimensionCommand() {
		return CommandEnum.DIMENSION_COMMAND.equals(parser.getType());
	}

	public boolean isGeneralCommand() {
		return WhereType.GENERAL.equals(parser.getWhereType());
	}

	public boolean isGroupCommand() {
		return WhereType.GROUP.equals(parser.getWhereType());
	}

	public boolean isEnvironmentCommand() {
		return WhereType.ENVIRONMENT.equals(parser.getWho());
	}

	public State getWhat() { // TODO change name => remove command
		return new State(StateName.STATUS, Convert.convertStatus(getWho(), parser.getWhat()));
	}

	public Who getWho() {
		return OpenWebNetWho.convert(parser.getWho());
	}

	public Where getWhere() {
		String where = parser.getWhere();
		return new Where(where, where);

	}

	public String getGroup() {
		return parser.getGroup();
	}

	public String getEnvironment() {
		return parser.getEnvironment();
	}

	public SpecialCommand getSpecialCommand() {
		return new SpecialCommand(parser);
	}

	public State getDimension() throws UnknownState {
		return Convert.convert(getWho(),  parser.getDimension(), parser.getDimensionList());
	}

	public String toString() {
		return command;
	}
}
