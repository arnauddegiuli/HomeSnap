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
import com.adgsoftware.mydomo.engine.oldconnector.UnknownControllerListener;

/**
 * Monitor listens event from the open server. Each time open
 * server receives an event (light on, outlet on, change temperature, ...)
 * it sends event to OpenWebMonitor.
 * OpenWebMonitor raise this event to concern controller registered.
 *
 */
public interface Monitor {

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
