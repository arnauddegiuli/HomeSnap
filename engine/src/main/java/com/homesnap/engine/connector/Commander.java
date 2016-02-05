package com.homesnap.engine.connector;

import com.homesnap.engine.controller.Controller;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

public interface Commander {

	/**
	 * Connect the commander instance to the bus.
	 * @return <code>true</code> if the connection has been established and <code>false</code> otherwise.
	 */
	public boolean connect();

	/**
	 * Disconnect the commander instance from the bus.
	 */
	public void disconnect();
	
	/**
	 * Indicates if the commander instance is connected
	 * @return <code>true</code> if connected and <code>false</code> otherwise.
	 */
	public boolean isConnected();

	/**
	 * Send a command to the open server.
	 * @param command the command to send
	 * @return the result of the command
	 */
	public void sendCommand(Command command, CommandListener resultListener);

	/**
	 * Add a controller which will be able to use this commander.
	 * @param controller
	 */
	public void addControllerToExecute(Controller controller);

	/**
	 * Remove a controller which is able to use this commander.
	 * @param controller
	 */
	public void removeControllerToExecute(Controller controller);

	/**
	 * Add a connectionListener.
	 * @param connectionListener
	 */
	public void addConnectionListener(ConnectionListener connectionListener);

	/**
	 * Remove a connectionListener.
	 * @param connectionListener
	 */
	public void removeConnectionListener(ConnectionListener connectionListener);
}
