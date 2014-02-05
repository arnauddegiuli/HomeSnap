package com.homesnap.engine.controller;

import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.UnknownControllerListener;

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
