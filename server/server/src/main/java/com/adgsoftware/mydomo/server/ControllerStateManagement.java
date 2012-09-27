package com.adgsoftware.mydomo.server;

import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.server.commandmodules.ControllerCommand;
import com.adgsoftware.mydomo.server.commandmodules.ControllerDimensionCommand;

public class ControllerStateManagement {

	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	private static Hashtable<String, List<DimensionValue>> dimensionList = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	private static Hashtable<String, ControllerCommand> controllerCommandList = new Hashtable<String, ControllerCommand>();
	private static Hashtable<String, ControllerDimensionCommand> controllerDimensionCommandList = new Hashtable<String, ControllerDimensionCommand>();
	
	public static void registerControllerCommand(ControllerCommand controllerCommand) {
		controllerCommandList.put(controllerCommand.getWho(), controllerCommand);
	}
	
	public static void unRegisterControllerCommand(ControllerCommand controllerCommand) {
		controllerCommandList.remove(controllerCommand.getWho());
	}
	
	public static void registerControllerDimensionCommand(ControllerDimensionCommand controllerDimensionCommand) {
		controllerDimensionCommandList.put(controllerDimensionCommand.getWho(), controllerDimensionCommand);
	}
	
	public static void unRegisterControllerDimensionCommand(ControllerDimensionCommand controllerDimensionCommand) {
		controllerDimensionCommandList.remove(controllerDimensionCommand.getWho());
	}
	
	protected static String getCommandResult(String command) {
		
		String who = Command.getWhoFromCommand(command);
		
		ControllerCommand cc = controllerCommandList.get(who);
		if (cc != null) {
			return cc.execute(command, statusList);
		} else {
			ControllerDimensionCommand cdc = controllerDimensionCommandList.get(who);
			if (cdc != null) {
				return cdc.execute(command, dimensionList);
			} else {
				System.out.println("Command not supported [" + command + "]");
				return Command.NACK;
			}
		}
	}
	
	protected static String getStatus(String command) {
		String who = Command.getWhoFromCommand(command);
		
		ControllerCommand cc = controllerCommandList.get(who);
		if (cc != null) {
			return cc.status(command, statusList);
		} else {
			ControllerDimensionCommand cdc = controllerDimensionCommandList.get(who);
			if (cdc != null) {
				return cdc.status(command, dimensionList);
			} else {
				System.out.println("Command not supported [" + command + "]");
				return Command.NACK;
			}
		}
	}
	
}
