package com.adgsoftware.mydomo.engine;

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

import java.util.ArrayList;
import java.util.List;

import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.parser.CommandParser;
import com.adgsoftware.mydomo.engine.parser.ParseException;

public class Command {
	// TODO refactor this class to move it to connector.impl... and remove
	// dependencies in other package. to be able to manage connection to xpl or
	// knx
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

	// WHO 28 €
	public final static String WHO_SCENARIO = "0";
	public final static String WHO_LIGHTING = "1";
	public final static String WHO_AUTOMATION = "2";
	public final static String WHO_POWER_MANAGEMENT = "3";
	public final static String WHO_HEATING_ADJUSTMENT = "4";
	public final static String WHO_MULTIMEDIA = "7";
	public final static String WHO_GATEWAY = "13";
	public final static String WHO_SOUND_SYSTEM = "16";
	public final static String WHO_DIAGNOSTIC_OF_HEATING_ADJUSTMENT = "1004";

	public static boolean isStandardCommand(String command) {
		try {
			return CommandEnum.STANDARD_COMMAND.equals(CommandParser.parse(
					command).getType());
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return false;
		}
	}

	public static boolean isDimensionCommand(String command) {
		try {
			return CommandEnum.DIMENSION_COMMAND.equals(CommandParser.parse(
					command).getType());
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return false;
		}
	}

	public static boolean isGeneralCommand(String command) {
		try {
			String who = CommandParser.parse(command).getWho();
			if (who.equals("0")) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return false;
		}
	}

	public static boolean isGroupCommand(String command) {
		try {
			String who = CommandParser.parse(command).getWho();
			if (who.startsWith("#")) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return false;
		}
	}

	public static boolean isAmbianceCommand(String command) {
		try {
			String who = CommandParser.parse(command).getWho();
			if (who.length() == 1) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return false;
		}
	}

	public static String getWhatFromCommand(String command) {
		try {
			return CommandParser.parse(command).getWhat();
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return null;
		}
	}

	public static String getWhoFromCommand(String command) {
		try {
			return CommandParser.parse(command).getWho();
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return null;
		}
	}

	public static String getWhereFromCommand(String command) {
		try {
			return CommandParser.parse(command).getWhere();
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return null;
		}
	}

	public static String getDimensionFromCommand(String command) {

		try {
			return CommandParser.parse(command).getDimension();
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
		}
		return null;
	}

	public static List<DimensionValue> getDimensionListFromCommand(
			String command) {
		List<DimensionValue> dimensionList = new ArrayList<DimensionValue>();
		try {
			for (String dimension : CommandParser.parse(command)
					.getDimensionList()) {
				DimensionValue d = new DimensionValueImpl();
				d.setValue(dimension);
				dimensionList.add(d);
			}
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
		}
		return dimensionList;
	}
}
