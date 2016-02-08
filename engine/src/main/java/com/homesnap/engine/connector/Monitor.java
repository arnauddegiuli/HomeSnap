package com.homesnap.engine.connector;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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

import com.homesnap.engine.controller.Controller;

/**
 * Monitor listens event from the open server. Each time open
 * server receives an event (light on, outlet on, change temperature, ...)
 * it sends event to OpenWebMonitor.
 * OpenWebMonitor raise this event to concern controller registered.
 *
 */
public interface Monitor {

	/**
	 * Connect the monitor instance to the bus.
	 * @return <code>true</code> if the connection has been established and <code>false</code> otherwise.
	 */
	public boolean connect();

	/**
	 * Disconnect the monitor instance from the bus.
	 */
	public void disconnect();
	
	/**
	 * Indicates if the monitor instance is connected
	 * @return <code>true</code> if connected and <code>false</code> otherwise.
	 */
	public boolean isConnected();

	/**
	 * Register controller to listen event.
	 * @param controller
	 */
	public void addControllerToMonitor(Controller controller);

	/**
	 * Remove controller which listen event.
	 * @param controller
	 */
	public void removeControllerToMonitor(Controller controller);

	/**
	 * Add a {@link ConnectionListener}
	 * @param connectionListener
	 */
	public void addConnectionListener(ConnectionListener connectionListener);

	/**
	 * Remove a {@link ConnectionListener}
	 * @param connectionListener
	 */
	public void removeConnectionListener(ConnectionListener connectionListener);

	/**
	 * Add a {@link CommandListener}
	 * @param commandListener
	 */
	public void addControllerStatusListener(CommandListener commandListener);

	/**
	 * Remove a {@link CommandListener}
	 * @param commandListener
	 */
	public void removeControllerStatusListener(CommandListener commandListener);

	/**
	 * Add an {@link UnknownControllerListener}
	 * @param unknownControllerListener
	 */
	public void addUnknownControllerListener(UnknownControllerListener unknownControllerListener);

	/**
	 * Remove an {@link UnknownControllerListener}
	 * @param unknownControllerListener
	 */
	public void removeUnknownControllerListener(UnknownControllerListener unknownControllerListener);
}
