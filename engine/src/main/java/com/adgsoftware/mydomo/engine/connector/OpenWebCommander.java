package com.adgsoftware.mydomo.engine.connector;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public interface OpenWebCommander {

	/**
	 * Connect to the open server.
	 * @return true if connected to the open server
	 */
	public abstract boolean connect();
	
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
	public abstract CommandResult sendCommand(String command);

	public abstract void addControllerToExecute(Controller<? extends Status> controller);
	
	public abstract void addConnectionListener(OpenWebConnectionListener connectionListener);
	
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