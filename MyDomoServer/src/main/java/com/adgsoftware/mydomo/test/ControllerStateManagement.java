package com.adgsoftware.mydomo.test;

import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.test.controller.commandmodules.GatewayCommand;
import com.adgsoftware.mydomo.test.controller.commandmodules.LightCommand;

public class ControllerStateManagement {

	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	private static Hashtable<String, List<DimensionValue>> dimensionList = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	public static String getCommandResult(String command) {
		
		String who = Command.getWhoFromCommand(command);
		
		if (Command.WHO_LIGHTING.equals(who)) {
			return LightCommand.execute(command, statusList);
		} else if (Command.WHO_GATEWAY.equals(who)){
			return GatewayCommand.execute(command, dimensionList);
			
		} else {
			System.out.println("Command not supported [" + command + "]");
			return Command.NACK;
		}
	}
	
	public static String getStatus(String command) {
		if (Command.WHO_LIGHTING.equals(Command.getWhoFromCommand(command))) {
			return LightCommand.status(command, statusList);
		} else if (Command.WHO_GATEWAY.equals(Command.getWhoFromCommand(command))) {
			return GatewayCommand.status(command, dimensionList);			
		} else {
			System.out.println("Command not supported [" + command + "]");
			return Command.NACK;
		}
	}
	
}
