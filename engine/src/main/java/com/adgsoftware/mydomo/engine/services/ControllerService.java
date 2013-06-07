package com.adgsoftware.mydomo.engine.services;

import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.UnknownControllerListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

/**
 * ControllerService implements feature about controller. 
 *
 */
public interface ControllerService {

	/**
	 * Create a controller. Controller created is already connected
	 * to open web server (with a monitor session and a command session):
	 * this means that when you get the status ({@link Controller#getStatus(String)})
	 * or change it, the order is sent to the open web server. 
	 * @param clazz the class of the controller you would like create.
	 * @param where the address of the device
	 * @return the new created and initialized controller
	 */
	public abstract <T extends Controller<? extends Status>> T createController(
			Class<T> clazz, String where);
	
	public abstract boolean isCommanderConnected();
	
	public abstract boolean isMonitorConnected();
	
	/**
	 * Define the ip of the gateway to connect.
	 * @param ip ipv4
	 */
	public abstract void setIp(String ip);
	
	/**
	 * Define the port of the gateway to connect.
	 * @param port
	 */
	public abstract void setPort(int port);

	public void onDestroy();
	
	/**
	 * Add a listener on new monitor session connection
	 * @param listener
	 */
	public void addMonitorConnectionListener(ConnectionListener listener);
	
	/**
	 * Add a listener on new command session connection
	 * @param listener
	 */
	public void addCommanderConnectionListener(ConnectionListener listener);
	
	/**
	 * Add a listener on new controller detected. A new controller is detected
	 * when monitor session get a message and doesn't know the where.
	 * @param listener
	 */
	public void addUnknowControllerListener(UnknownControllerListener listener);
}