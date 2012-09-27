package com.adgsoftware.mydomo.engine;

import java.util.ArrayList;
import java.util.List;

import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.parser.CommandParser;
import com.adgsoftware.mydomo.engine.parser.ParseException;

public class Command {

	// Standard          *WHO*WHAT*WHERE##
	// Status request    *#WHO*WHERE##
	// Dimension request *#WHO*WHERE*DIMENSION##
	// Dimension write   *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##	
	
	public final static String ACK = "*#*1##";
	public final static String NACK = "*#*0##";
	
	public final static String COMMAND_SESSION = "*99*0##";
	public final static String MONITOR_SESSION = "*99*1##";
	
	public final static String COMMAND = "*{0}*{1}*{2}##"; // *WHO*WHAT*WHERE##
	public final static String STATUS = "*#{0}*{1}##";     // *#WHO*WHERE##
	public final static String DIMENSION_STATUS = "*#{0}*{1}*{2}##"; // *#WHO*WHERE*DIMENSION## => response: *#WHO#*WHERE*WHATDIMENSION*VAL1*...*VALn##
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
			return CommandEnum.STANDARD_COMMAND.equals(CommandParser.parse(command).getType());
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			return false;
		}
	}
	
	public static boolean isDimensionCommand(String command) {
		try {
			return CommandEnum.DIMENSION_COMMAND.equals(CommandParser.parse(command).getType());
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
	
	public static DimensionValue getDimensionFromCommand(String command) {
		
		try {
				DimensionValue d = new DimensionValueImpl();
				d.setValue(CommandParser.parse(command).getDimension());
				return d;
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
		}
		return null;
	}
	
	public static List<DimensionValue> getDimensionListFromCommand(String command) {
		List<DimensionValue> dimensionList = new ArrayList<DimensionValue>();
		try {
			for (String dimension : CommandParser.parse(command).getDimensionList()) {
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
