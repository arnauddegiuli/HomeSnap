package com.adgsoftware.mydomo.server;

/*
 * #%L
 * MyDomoServer
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
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.oldconnector.openwebnet.Command;
import com.adgsoftware.mydomo.engine.oldconnector.openwebnet.parser.ParseException;
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
	public synchronized static void registerControllerCommand(ControllerSimulator controllerCommand) {
		synchronized (controllerCommandList) {
			controllerCommandList.put(controllerCommand.getWho(), controllerCommand);	
		}
		
	}
	
	/**
	 * Unregister a controller. Call by a module (for example light module) when user stop the module.
	 * @param controllerCommand controller to unregister
	 */
	public synchronized static void unRegisterControllerCommand(ControllerSimulator controllerCommand) {
		synchronized (controllerCommandList) {
			controllerCommandList.remove(controllerCommand.getWho());
		}
	}
	
	public synchronized static void registerControllerDimensionCommand(ControllerDimensionSimulator controllerDimensionCommand) {
		synchronized (controllerDimensionCommandList) {
			controllerDimensionCommandList.put(controllerDimensionCommand.getWho(), controllerDimensionCommand);	
		}
		
	}
	
	public synchronized static void unRegisterControllerDimensionCommand(ControllerDimensionSimulator controllerDimensionCommand) {
		synchronized (controllerDimensionCommandList) {
			controllerDimensionCommandList.remove(controllerDimensionCommand.getWho());
		}
	}

	public synchronized static void registerMonitorSession(MonitorSession monitor) {
		synchronized (monitorList) {
			monitorList.add(monitor);
		}
		
	}
	
	public synchronized static void unRegisterMonitorSession(MonitorSession monitor) {
		synchronized (monitorList) {
			monitorList.remove(monitor);
		}
	}	
	
	/**
	 * Simulate the execution of the command.
	 * @param command command to execute
	 * @return the result of the command
	 */
	public synchronized static String executeCommand(String command) {
		try {
			Command parser = Command.getCommandAnalyser(command);
			String who = parser.getWhoFromCommand();
			String result;
			ControllerSimulator cc;
			synchronized (controllerCommandList) {
				synchronized (controllerDimensionCommandList) {
				cc = controllerCommandList.get(who);
			
			if (cc != null) {
				result = cc.execute(command);
			} else {
				
					ControllerDimensionSimulator cdc = controllerDimensionCommandList.get(who);
					if (cdc != null) {
						result = cdc.execute(command);
					} else {
						System.out.println("Command not supported [" + command + "]");
						result = Command.NACK;
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
			}}
			return result;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Simulate the execution of the command (status request)
	 * @param command the status request to execute
	 * @return the status
	 */
	public synchronized static String executeStatus(String command) {
		try {
			Command parser = Command.getCommandAnalyser(command);
			String who = parser.getWhoFromCommand();
			ControllerSimulator cc;
			
			synchronized (controllerCommandList) {
				synchronized (controllerDimensionCommandList) {
				cc = controllerCommandList.get(who);
			
			if (cc != null) {
				return cc.status(command);
			} else {
				
					ControllerDimensionSimulator cdc = controllerDimensionCommandList.get(who);
					if (cdc != null) {
						return cdc.status(command);
					} else {
						System.out.println("Command not supported [" + command + "]");
						return Command.NACK;
					}
				}
			}}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
