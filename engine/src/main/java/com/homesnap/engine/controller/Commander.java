package com.homesnap.engine.controller;

import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;

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
