package com.adgsoftware.mydomo.engine.connector;

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

import com.adgsoftware.mydomo.engine.controller.CommandListener;
import com.adgsoftware.mydomo.engine.controller.Controller;

/**
 * Monitor listens event from the open server. Each time open
 * server receives an event (light on, outlet on, change temperature, ...)
 * it sends event to OpenWebMonitor.
 * OpenWebMonitor raise this event to concern controller registered.
 *
 */
public interface Monitor {

	/**
	 * Connect to the open server.
	 * @return true if connected to the open server
	 */
	public abstract boolean connect();

	/**
	 * Close connection to the open server.
	 */
	public abstract void close();

	/**
	 * Register controller to listen event.
	 * @param controller
	 */
	public abstract void addControllerToMonitor(Controller controller);

	/**
	 * Remove controller which listen event.
	 * @param controller
	 */
	public abstract void removeControllerToMonitor(Controller controller);

	/**
	 * Add a {@link ConnectionListener}
	 * @param connectionListener
	 */
	public abstract void addConnectionListener(ConnectionListener connectionListener);

	/**
	 * Remove a {@link ConnectionListener}
	 * @param connectionListener
	 */
	public abstract void removeConnectionListener(ConnectionListener connectionListener);

	/**
	 * Add a {@link CommandListener}
	 * @param commandListener
	 */
	public abstract void addControllerStatusListener(CommandListener commandListener);

	/**
	 * Remove a {@link CommandListener}
	 * @param commandListener
	 */
	public abstract void removeControllerStatusListener(CommandListener commandListener);

	/**
	 * Add an {@link UnknownControllerListener}
	 * @param unknownControllerListener
	 */
	public abstract void addUnknownControllerListener(UnknownControllerListener unknownControllerListener);

	/**
	 * Remove an {@link UnknownControllerListener}
	 * @param unknownControllerListener
	 */
	public abstract void removeUnknownControllerListener(UnknownControllerListener unknownControllerListener);

	/**
	 * Return true if connected to the open server.
	 * @return true if connected to the open server.
	 */
	public abstract boolean isConnected();
}