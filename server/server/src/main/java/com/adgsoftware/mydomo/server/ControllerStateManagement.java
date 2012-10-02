package com.adgsoftware.mydomo.server;

import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.server.commandmodules.ControllerCommand;
import com.adgsoftware.mydomo.server.commandmodules.ControllerDimensionCommand;

public class ControllerStateManagement {

	/**
	 * TODO: nice to have but should be better to use OSGi service registry than static way... 
	 */
	private static Hashtable<String, String> statusList = new Hashtable<String, String>(); // where, what
	private static Hashtable<String, List<DimensionValue>> dimensionList = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	private static Hashtable<String, ControllerCommand> controllerCommandList = new Hashtable<String, ControllerCommand>();
	private static Hashtable<String, ControllerDimensionCommand> controllerDimensionCommandList = new Hashtable<String, ControllerDimensionCommand>();
	
	/**
	 * Register a new ControllerCommand. Call by a module (for example light module) to register it to the server.
	 * @param controllerCommand controller to register
	 */
	public static void registerControllerCommand(ControllerCommand controllerCommand) {
		controllerCommandList.put(controllerCommand.getWho(), controllerCommand);
	}
	
	/**
	 * Unregister a controller. Call by a module (for example light module) when user stop the module.
	 * @param controllerCommand controller to unregister
	 */
	public static void unRegisterControllerCommand(ControllerCommand controllerCommand) {
		controllerCommandList.remove(controllerCommand.getWho());
	}
	
	public static void registerControllerDimensionCommand(ControllerDimensionCommand controllerDimensionCommand) {
		controllerDimensionCommandList.put(controllerDimensionCommand.getWho(), controllerDimensionCommand);
	}
	
	public static void unRegisterControllerDimensionCommand(ControllerDimensionCommand controllerDimensionCommand) {
		controllerDimensionCommandList.remove(controllerDimensionCommand.getWho());
	}
	
	/**
	 * Simulate the execution of the command.
	 * @param command command to execute
	 * @return the result of the command
	 */
	protected static String executeCommand(String command) {
		
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
	
	/**
	 * Simulate the execution of the command (status request)
	 * @param command the status request to execute
	 * @return the status
	 */
	protected static String executeStatus(String command) {
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
