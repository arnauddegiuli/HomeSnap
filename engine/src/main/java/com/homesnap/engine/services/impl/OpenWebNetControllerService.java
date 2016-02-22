package com.homesnap.engine.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.Command.Type;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.CommandResult;

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


import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.ConnectionListener;
import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.connector.UnknownControllerListener;
import com.homesnap.engine.connector.openwebnet.OpenWebCommanderImpl;
import com.homesnap.engine.connector.openwebnet.OpenWebMonitorImpl;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.light.Light;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.ScanListener;

public class OpenWebNetControllerService implements ControllerService {

	private Log log = new Log();
	private Monitor monitor;
	private Monitor monitorScan;
	private Commander commander;
	private String host;
	private int port;
	private Integer passwordOpen;
	private List<ScanListener> scanListenerList = new ArrayList<ScanListener>();
	
	
	/**
	 * Constructor.
	 * @param host the hostname or ip of My Open Server
	 * @param port the port number of My Open Server
	 * @param password the passwod of My Open Server (null if not)
	 */
	public OpenWebNetControllerService(String host, int port, Integer passwordOpen) {
		this.host = host;
		this.port = port;
		this.passwordOpen = passwordOpen;
	}
	
	/* (non-Javadoc)
	 * @see com.homesnap.engine.services.ControllerService#createController(com.homesnap.engine.Category, java.lang.String)
	 */
	@Override
	public <T extends Controller> T createController(
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
			controller.setWhere(new Where(where, where));
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
			monitor.disconnect();
		}
		
		if (commander != null) {
			commander.disconnect();
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
		((OpenWebCommanderImpl) this.getOpenWebCommand()).setIp(ip);
		((OpenWebMonitorImpl) this.getOpenWebMonitor()).setIp(ip);
	}

	@Override
	public void setPort(int port) {
		((OpenWebCommanderImpl) this.getOpenWebCommand()).setPort(port);
		((OpenWebMonitorImpl) this.getOpenWebMonitor()).setPort(port);
	}
	
	@Override
	public void setPassword(Integer password) {
		((OpenWebCommanderImpl) this.getOpenWebCommand()).setPasswordOpen(password);
		((OpenWebMonitorImpl) this.getOpenWebMonitor()).setPasswordOpen(password);
	}

	@Override
	public void addCommanderConnectionListener(ConnectionListener listener) {
		this.getOpenWebCommand().addConnectionListener(listener);
		
	}

	@Override
	public void removeCommanderConnectionListener(ConnectionListener listener) {
		this.getOpenWebCommand().removeConnectionListener(listener);
		
	}
	
	@Override
	public void addMonitorConnectionListener(ConnectionListener listener) {
		this.getOpenWebMonitor().addConnectionListener(listener);
		
	}

	@Override
	public void removeMonitorConnectionListener(ConnectionListener listener) {
		this.getOpenWebMonitor().removeConnectionListener(listener);
		
	}

	@Override
	public void addUnknowControllerListener(UnknownControllerListener arg0) {
		this.getOpenWebMonitor().addUnknownControllerListener(arg0);
	}
	
	@Override
	public void removeUnknowControllerListener(UnknownControllerListener arg0) {
		this.getOpenWebMonitor().removeUnknownControllerListener(arg0);
	}
	
	@Override
	public void connect() {
		if (!isCommanderConnected()) {
		this.getOpenWebCommand().connect();
		}
		if (!isMonitorConnected()) {
			this.getOpenWebMonitor().connect();
		}
	}

	@Override
	public void addScanListener(ScanListener listener) {
		scanListenerList.add(listener);
	}
	
	@Override
	public void removeScanListener(ScanListener listener) {
		scanListenerList.remove(listener);
	}
	
	private Monitor getOpenWebMonitorScan() {
		if (monitorScan == null) {
			monitorScan = new OpenWebMonitorImpl(host, port, passwordOpen);
			UnknownControllerListener l = new UnknownControllerListener() {
				
				@Override
				public void foundUnknownController(Who who, Where where, List<What> what) {
					for(ScanListener listener : scanListenerList) {
						if (Who.LIGHT.equals(who)) {
							listener.foundController(who, where, createController(Light.class, where.getFrom()));
						} else {
							listener.foundController(who, where, null);
						}
					}	
				}
			};
			monitorScan.addUnknownControllerListener(l);
		}
		
		return monitorScan;
	}
	
	@Override
	public void scan() {
		monitor = getOpenWebMonitorScan();
		commander = getOpenWebCommand();
		
		
		for (int i= 1; i < 10; i++) {
			for(ScanListener listener : scanListenerList) {
				listener.progess(i*10);
			}
			
			// command i
			Command command = new Command(Who.LIGHT, new What("status", null), new Where(""+i, ""+i), Type.STATUS, null);
			final int where = i;
			commander.sendCommand(command, new CommandListener() {
				
				@Override
				public void onCommand(CommandResult commandResult) {
					log.finest(Session.Command, "Send status command to address[" + where + ":" + commandResult.getStatus().name() +"]");
					
				}
			});
			
		}
		
		for(ScanListener listener : scanListenerList) {
			listener.progess(100);
		}
		
	}
}
