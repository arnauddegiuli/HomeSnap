package com.adgsoftware.mydomo.engine.services;

import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
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
	
	public abstract void setIp(String ip);
	
	public abstract void setPort(int port);

	public void onDestroy();
	
	public void addMonitorConnectionListener(ConnectionListener listener);
	
	public void addCommanderConnectionListener(ConnectionListener listener);
}