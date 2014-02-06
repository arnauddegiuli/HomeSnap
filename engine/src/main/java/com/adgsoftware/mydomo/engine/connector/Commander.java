package com.adgsoftware.mydomo.engine.connector;

import com.adgsoftware.mydomo.engine.controller.Command;
import com.adgsoftware.mydomo.engine.controller.CommandListener;
import com.adgsoftware.mydomo.engine.controller.Controller;

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

public interface Commander {

	/**
	 * Connect to the open server.
	 */
	public abstract void connect();
	
	/**
	 * Return true if connected to the open server.
	 * @return true if connected to the open server.
	 */
	public abstract boolean isConnected();

	/**
	 * Close connection to the open server.
	 */
	public abstract void close();

	/**
	 * Send a command to the open server.
	 * @param command the command to send
	 * @return the result of the command
	 */
	public abstract void sendCommand(Command command, CommandListener resultListener);

	/**
	 * Add a controller which will be able to use this commander.
	 * @param controller
	 */
	public abstract void addControllerToExecute(Controller controller);

	/**
	 * Remove a controller which is able to use this commander.
	 * @param controller
	 */
	public abstract void removeControllerToExecute(Controller controller);

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