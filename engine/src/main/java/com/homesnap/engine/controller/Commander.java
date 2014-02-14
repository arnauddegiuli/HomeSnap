package com.homesnap.engine.controller;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import com.adgsoftware.mydomo.engine.oldconnector.CommandListener;
import com.adgsoftware.mydomo.engine.oldconnector.ConnectionListener;

/**
 * Monitor listens event from the open server. Each time open
 * server receives an event (light on, outlet on, change temperature, ...)
 * it sends event to OpenWebMonitor.
 * OpenWebMonitor raise this event to concern controller registered.
 *
 */
public interface Commander {
	
	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public boolean connect();
	
	/**
	 * 
	 * @return
	 */
	public boolean isConnected();

	/**
	 * 
	 */
	public void disconnect();
	
	/**
	 * 
	 * @param command
	 * @param resultListener
	 */
	public void sendCommand(String command, CommandListener resultListener);
	
	/**
	 * 
	 * @param command
	 * @param resultListener
	 */
	public void sendCommand(Command command, CommandListener resultListener);

	/**
	 * 
	 * @param controller
	 */
	public void addControllerToControl(Controller controller);

	/**
	 * 
	 * @param controller
	 */
	public void removeControllerToControl(Controller controller);

	/**
	 * Add a connectionListener.
	 * @param connectionListener
	 */
	public abstract void addConnectionListener(ConnectionListener connectionListener);

	/**
	 * Remove a connectionListener.
	 * @param connectionListener
	 */
	public abstract void removeConnectionListener(ConnectionListener connectionListener);
}
