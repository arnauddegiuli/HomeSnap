package com.adgsoftware.mydomo.engine.connector.openwebnet;

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

import java.util.List;

import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionValue;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.CommandParser;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;
import com.adgsoftware.mydomo.engine.controller.what.State;
import com.adgsoftware.mydomo.engine.controller.what.StateName;
import com.adgsoftware.mydomo.engine.controller.where.Where;
import com.adgsoftware.mydomo.engine.controller.who.Who;

public class OpenWebNetCommand {

	private String command;
	private CommandParser parser;

	public OpenWebNetCommand(String command) throws ParseException {
		try {
			parser = CommandParser.parse(command);
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			throw e;
		}
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
		return new State(StateName.STATUS, StatusMapping.convert(getWho(), parser.getWhat()));
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

	public State getDimension() {
		String code = parser.getDimension();
		List<DimensionValue> dimensionList = parser.getDimensionList();
		return DimensionMapping.convert(new DimensionStatusImpl(dimensionList, code));
	}

	public String toString() {
		return command;
	}
}
