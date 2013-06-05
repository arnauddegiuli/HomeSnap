package com.adgsoftware.mydomo.server;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.server.controllermodules.ControllerDimensionSimulator;
import com.adgsoftware.mydomo.server.controllermodules.ControllerSimulator;

public class ControllerStateManagement {
	
	private static Hashtable<String, ControllerSimulator> controllerCommandList = new Hashtable<String, ControllerSimulator>();
	private static Hashtable<String, ControllerDimensionSimulator> controllerDimensionCommandList = new Hashtable<String, ControllerDimensionSimulator>();
	
	private static List<MonitorSession> monitorList = new ArrayList<MonitorSession>();
	
	/**
	 * Register a new ControllerCommand. Call by a module (for example light module) to register it to the server.
	 * @param controllerCommand controller to register
	 */
	public static void registerControllerCommand(ControllerSimulator controllerCommand) {
		synchronized (controllerCommandList) {
			controllerCommandList.put(controllerCommand.getWho(), controllerCommand);	
		}
		
	}
	
	/**
	 * Unregister a controller. Call by a module (for example light module) when user stop the module.
	 * @param controllerCommand controller to unregister
	 */
	public static void unRegisterControllerCommand(ControllerSimulator controllerCommand) {
		synchronized (controllerCommandList) {
			controllerCommandList.remove(controllerCommand.getWho());
		}
	}
	
	public static void registerControllerDimensionCommand(ControllerDimensionSimulator controllerDimensionCommand) {
		synchronized (controllerDimensionCommandList) {
			controllerDimensionCommandList.put(controllerDimensionCommand.getWho(), controllerDimensionCommand);	
		}
		
	}
	
	public static void unRegisterControllerDimensionCommand(ControllerDimensionSimulator controllerDimensionCommand) {
		synchronized (controllerDimensionCommandList) {
			controllerDimensionCommandList.remove(controllerDimensionCommand.getWho());
		}
	}

	public static void registerMonitorSession(MonitorSession monitor) {
		synchronized (monitorList) {
			monitorList.add(monitor);
		}
		
	}
	
	public static void unRegisterMonitorSession(MonitorSession monitor) {
		synchronized (monitorList) {
			monitorList.remove(monitor);
		}
	}	
	
	/**
	 * Simulate the execution of the command.
	 * @param command command to execute
	 * @return the result of the command
	 */
	public static String executeCommand(String command) {
		
		String who = Command.getWhoFromCommand(command);
		String result;
		ControllerSimulator cc;
		synchronized (controllerCommandList) {
			cc = controllerCommandList.get(who);
		}
		if (cc != null) {
			result = cc.execute(command);
		} else {
			synchronized (controllerDimensionCommandList) {
				ControllerDimensionSimulator cdc = controllerDimensionCommandList.get(who);
				if (cdc != null) {
					result = cdc.execute(command);
				} else {
					System.out.println("Command not supported [" + command + "]");
					result = Command.NACK;
				}
			}
		}
		
		if (!Command.NACK.equalsIgnoreCase(result)) {
			synchronized (monitorList) {
				// Monitor session closed is only detected when we try to lunch a command on it
				// So, here we clone the monitor list since in monitor(command) method, if monitor session has been closed,
				// it is removed from the monitorList => cause a concurrent modification not prevented by the lock because we
				// are in the same thread...
				List<MonitorSession> monitorList2 = new ArrayList<MonitorSession>(monitorList);
				for (MonitorSession monitor : monitorList2) {
					monitor.monitor(command);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Simulate the execution of the command (status request)
	 * @param command the status request to execute
	 * @return the status
	 */
	public static String executeStatus(String command) {
		String who = Command.getWhoFromCommand(command);
		ControllerSimulator cc;
		
		synchronized (controllerCommandList) {
			cc = controllerCommandList.get(who);
		}
		if (cc != null) {
			return cc.status(command);
		} else {
			synchronized (controllerDimensionCommandList) {
				ControllerDimensionSimulator cdc = controllerDimensionCommandList.get(who);
				if (cdc != null) {
					return cdc.status(command);
				} else {
					System.out.println("Command not supported [" + command + "]");
					return Command.NACK;
				}
			}
		}
	}
	
}
