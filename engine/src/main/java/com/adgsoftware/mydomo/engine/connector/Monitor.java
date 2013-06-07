package com.adgsoftware.mydomo.engine.connector;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

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
	public abstract void addControllerToMonitor(Controller<? extends Status> controller);
	
	public abstract void addConnectionListener(ConnectionListener connectionListener);
	
	public abstract void addControllerStatusListener(CommandListener commandListener);
	
	public abstract void addUnknownControllerListener(UnknownControllerListener unknownControllerListener);
	
	/**
	 * Return true if connected to the open server.
	 * @return true if connected to the open server.
	 */
	public abstract boolean isConnected();

	/**
	 * Return the ip of the open server.
	 * @return the ip of the open server.
	 */
	public String getIp();

	/**
	 * Define the ip of the open server.
	 * @param ip the ip of the open server.
	 */
	public void setIp(String ip);

	/**
	 * Return the port of the open server.
	 * @return the port of the open server.
	 */
	public int getPort();

	/**
	 * Define the port of the open server.
	 * @param port the port of the open server.
	 */
	public void setPort(int port);
	
	/**
	 * Return the timeout of the connection to open server in millisecond.
	 * @return the timeout of the connection to open server in millisecond.
	 */
	public int getTimeout();

	/**
	 * Define the timeout of the connection to open server in millisecond.
	 * @param timeout the timeout of the connection to open server in millisecond.
	 */
	public void setTimeout(int timeout);
}
