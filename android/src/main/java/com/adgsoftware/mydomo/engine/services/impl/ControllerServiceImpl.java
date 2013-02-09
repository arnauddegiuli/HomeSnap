package com.adgsoftware.mydomo.engine.services.impl;

import com.adgsoftware.mydomo.engine.connector.Commander;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.Monitor;
import com.adgsoftware.mydomo.engine.connector.impl.OpenWebCommanderImpl;
import com.adgsoftware.mydomo.engine.connector.impl.OpenWebMonitorImpl;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.services.ControllerService;

public class ControllerServiceImpl implements ControllerService {

	private Monitor monitor;
	private Commander commander;
	private String host;
	private int port;
	private long passwordOpen = 0L;
	
	
	/**
	 * Constructor.
	 * @param host the hostname or ip of My Open Server
	 * @param port the port number of My Open Server
	 */
	public ControllerServiceImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/* (non-Javadoc)
	 * @see com.adgsoftware.mydomo.engine.services.ControllerService#createController(com.adgsoftware.mydomo.engine.Category, java.lang.String)
	 */
	@Override
	public <T extends Controller<? extends Status>> T createController(
			Class<T> clazz, String where) {
		
		T controller = null;
		
		try {
			controller = (T) clazz.newInstance();
			Monitor mon = getOpenWebMonitor();
			if (mon != null) {
			 mon.addControllerToMonitor(controller);
			}
			Commander com = getOpenWebCommand();
			if ( com != null )  {
				com.addControllerToExecute(controller);
			}
			controller.setWhere(where);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return controller;
	}
	
	private Monitor getOpenWebMonitor() {
		if (monitor == null) {
			monitor = new OpenWebMonitorImpl(host, port, passwordOpen);
		}
		
		return monitor;
	}
	
	private Commander getOpenWebCommand() {
		if (commander == null) {
			commander = new OpenWebCommanderImpl(host, port, passwordOpen);
		}
		
		return commander;
	}
	
	public void onDestroy() {
		if (monitor != null) {
			monitor.close();
		}
		
		if (commander != null) {
			commander.close();
		}
	}

	@Override
	public boolean isCommanderConnected() {
		return this.getOpenWebCommand().isConnected();
	}

	@Override
	public boolean isMonitorConnected() {
		return this.getOpenWebMonitor().isConnected();
	}

	@Override
	public void setIp(String ip) {
		this.getOpenWebCommand().setIp(ip);
		this.getOpenWebMonitor().setIp(ip);
	}

	@Override
	public void setPort(int port) {
		this.getOpenWebCommand().setPort(port);
		this.getOpenWebMonitor().setPort(port);
	}

	@Override
	public void addCommanderConnectionListener(ConnectionListener listener) {
		this.getOpenWebCommand().addConnectionListener(listener);
		
	}

	@Override
	public void addMonitorConnectionListener(ConnectionListener listener) {
		this.getOpenWebMonitor().addConnectionListener(listener);
		
	}
}
