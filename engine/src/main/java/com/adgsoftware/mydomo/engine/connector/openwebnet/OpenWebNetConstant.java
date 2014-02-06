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

import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.CommandParser;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;

public class OpenWebNetConstant {
	// Standard *WHO*WHAT*WHERE##
	// Status request *#WHO*WHERE##
	// Dimension request *#WHO*WHERE*DIMENSION##
	// Dimension write *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##

	public final static String ACK = "*#*1##";
	public final static String NACK = "*#*0##";

	public final static String COMMAND_SESSION = "*99*0##";
	public final static String MONITOR_SESSION = "*99*1##";

	public final static String COMMAND = "*{0}*{1}*{2}##"; // *WHO*WHAT*WHERE##
	public final static String STATUS = "*#{0}*{1}##"; // *#WHO*WHERE##
	public final static String DIMENSION_STATUS = "*#{0}*{1}*{2}##"; // *#WHO*WHERE*DIMENSION##
																		// =>
																		// response:
																		// *#WHO#*WHERE*WHATDIMENSION*VAL1*...*VALn##
	public final static String DIMENSION_COMMAND = "*#{0}*{1}*#{2}*{3}##"; // *#WHO*WHERE*#WHATDIMENSION*DIMESION1*...*Dimensionn##
	public final static String DIMENSION_SEPARATOR = "*";



	// private constructor
	private OpenWebNetConstant(String command) throws ParseException {parser = CommandParser.parse(command);}

	private CommandParser parser;

	public static OpenWebNetConstant getCommandAnalyser(String command) throws ParseException {
		try {
			return new OpenWebNetConstant(command);
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

	public String getWhatFromCommand() {
		return parser.getWhat();
	}

	public String getWhoFromCommand() {
		return parser.getWho();
	}

	public String getWhereFromCommand() {
		return parser.getWhere();
	}

	public String getDimensionFromCommand() {
		return parser.getDimension();
	}

	public String getGroupFromCommand() {
		return parser.getGroup();
	}

	public String getEnvironmentFromCommand() {
		return parser.getEnvironment();
	}

	public SpecialCommand getSpecialCommand() {
		return new SpecialCommand(parser);
	}
}
