package com.adgsoftware.mydomo.engine.connector.impl;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionStatusEnum;
import com.adgsoftware.mydomo.engine.connector.Monitor;
import com.adgsoftware.mydomo.engine.connector.UnknownControllerListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.DimensionStatus;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.Status;

public class OpenWebMonitorImpl extends Thread 
implements Monitor {

	private Log log = new Log();
	private Integer passwordOpen = null;

	private Socket socket = null;	
	private BufferedReader depuisClient = null;
	private PrintWriter versClient = null;
	private String ip;
	private int port;
	private int timeout = 5000;
	private List<ConnectionListener> connectionListenerList = new ArrayList<ConnectionListener>();
	private List<CommandListener> commandListenerList = new ArrayList<CommandListener>();
	private List<UnknownControllerListener> unknownControllerListenerList = new ArrayList<UnknownControllerListener>();
	private List<Controller<? extends Status>> controllerList = new ArrayList<Controller<? extends Status>>();
	private boolean tryToConnect = true;
	private Password passwordEncoder = new Password();

	/**
	 * 
	 * @param ip the ip or dns name of the open server
	 * @param port the port number of the open server
	 * @param passwordOpen not supported actually
	 */
	public OpenWebMonitorImpl(String ip, int port, Integer passwordOpen){ 
		this.ip = ip;
		this.port = port;
		this.passwordOpen = passwordOpen;
		this.start();
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public void setIp(String ip) {
		this.ip = ip;
		resetSocket();
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
		resetSocket();
	}

	@Override
	public void setPasswordOpen(Integer passwordOpen) {
		this.passwordOpen = passwordOpen;
	}

	@Override
	public void addControllerToMonitor(Controller<? extends Status> controller) {
		controllerList.add(controller);
	}
	
	protected void onMessageReceipt(String message) {
		String where = Command.getWhereFromCommand(message);
		String what = Command.getWhatFromCommand(message);
		String who = Command.getWhoFromCommand(message);
		boolean known = false;
		
		if (Command.isGeneralCommand(message)) {
			// We send command to all correct address
			for (int i = 11; i < 99; i++) {
				if (i % 10 != 0) { // group address (20, 30, ..) are not correct
					known &= updateController(who, what, "" + i, message);
				}
			}
		} else if (Command.isGroupCommand(message)) {
			// We send command to group address
			
		} else if (Command.isAmbianceCommand(message)) {
			// We send ambiance command to address
			for (int i = 1; i < 9; i++) {
				known &= updateController(who, what, where + i, message);
			}
		} else {
			// Command direct on a controller
			known = updateController(who, what, where, message);
		}
		
		if (!known) {
			// Detected unknown device
			synchronized (unknownControllerListenerList) {
				for (UnknownControllerListener listener : unknownControllerListenerList) {
					listener.foundUnknownController( Command.getWhoFromCommand(message), where, what, Command.getDimensionFromCommand(message), Command.getDimensionListFromCommand(message));
				}
			}
		}
	}

	private boolean updateController(String who, String what, String where, String message) {
		boolean known = false;
		if (what != null) {
			// Manage what command
			for (Controller<? extends Status> controller : controllerList) {
				if (controller.getWho().equals(who) && controller.getWhere().equals(where)) {
					known = true;
					changeWhat(controller, what);
				}
			}
		} else {
			// Manage dimension command
			List<DimensionValue> dimensionList = Command.getDimensionListFromCommand(message);
			String code = Command.getDimensionFromCommand(message);
			for (Controller<? extends Status> controller : controllerList) {
				if (controller.getWho().equals(who) && controller.getWhere().equals(where)) {
					known = true;
					if (controller instanceof ControllerDimension<?>){
						changeDimension((ControllerDimension<? extends Status>) controller, code, dimensionList);// TODO: le changeWhat relance l'action.... => pas bon!
					}
					else {
						// TODO error!!!
					}
				}
			}
		}

		return known;
	}

	// To use generic: only at the runtime we will know the type so this is only method to set what!
	private static <T extends Status> void changeWhat(Controller<T> controller, String code) {
		T status = controller.getStatus(code);
		if (status != controller.getWhat()) { // If status has changed
			controller.changeWhat(status);
		}
	}

	// To use generic: only at the runtime we will know the type so this is only method to set what!
	private static void changeDimension(ControllerDimension<? extends Status> controller, String code, List<DimensionValue> dimensionList) {
		DimensionStatus dimensionStatus = controller.getDimensionStatusFromCache(code);
		if (dimensionStatus == null || dimensionStatus.getValueList() != dimensionList) { // dimensionStatus = null at before being initiated... it can happens when we get value before initiated process is finished...
			controller.changeDimensionStatus(dimensionStatus);
		}
	}

	public void run() {
		long timer = 1000;
		do {
			if (isConnected()) {
				// If connected read data
				String msg = read();
				if (msg != null) {
					onMessageReceipt(msg);
				}
			} else {
				// If not connected try to connect
				if (connect()) {
					// Now connected = reset
					timer = 1000;
				} else {
					// Impossible to connect, try later
					timer *= 2;
					try {
						sleep(timer);
					} catch (InterruptedException e) {
						log.finest(Log.Session.Monitor, "Timeout interrupted.");
					}
				}
			}
		} while (tryToConnect);
	}

	@Override
	public boolean connect() {
		try {
			log.fine(Log.Session.Monitor, "Connect to socket ["+ ip +":"+ port+"]");
			socket = new Socket();
			InetSocketAddress address = new InetSocketAddress(ip, port);
			socket.connect(address, getTimeout());
			depuisClient= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			log.finest(Log.Session.Monitor, "InputReader created.");
			versClient = new PrintWriter(socket.getOutputStream(),true);
			log.finest(Log.Session.Monitor, "PrintWriter created.");
		}catch (Exception e){
			log.severe(Log.Session.Monitor, "Impossible to connect to the server ["+ip+":"+port+"]");
			if (socket!=null && socket.isConnected()) {
				this.resetSocket();
			}
			return false;
		}

		if(socket != null){
			
				log.finest(Log.Session.Monitor, "----- Step Connection ----- ");
				String msg = read(); // TODO thread!
				if (!Command.ACK.equals(msg)) {
					// Bad return message
					log.severe(Log.Session.Monitor, "Bad message [" + msg + "] received from [" + ip + "]");
					callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
					this.resetSocket();
					return false;
				}

				log.finest(Log.Session.Monitor, "----- Step Identification -----");
				write(Command.MONITOR_SESSION);

				if(passwordOpen != null){
					msg = read();
					log.finest(Log.Session.Monitor, "\n----- Step authentification -----");
					log.finest(Log.Session.Monitor, "Rx: " + msg);
					msg = msg.substring(2); // Remove *#
					msg = msg.substring(0, msg.length()-2); // Remove last ##
					String password = passwordEncoder.calcPass(passwordOpen, msg);
					String passwordMsg = "*#"+password+"##"; 
					log.finest(Log.Session.Monitor, "Tx: " + passwordMsg);
					write(passwordMsg);		    	
				}
				log.finest(Log.Session.Monitor, "----- Step Final -----");
				msg = read();

				if (!Command.ACK.equals(msg)) {		       	
					log.severe(Log.Session.Monitor, "Problem during connection to [" + ip + "] with message [" + msg + "]");
					callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
					this.resetSocket();
					return false;
				}

				log.fine(Log.Session.Monitor, "Connection OK");
				callOpenWebConnectionListenerConnect(ConnectionStatusEnum.Connected);
				return true;
			} else {
				log.severe(Log.Session.Monitor, "No socket... Impossible to connect");
				callOpenWebConnectionListenerConnect(ConnectionStatusEnum.NoSocket);
				return false;
			}
	}

	private void callOpenWebConnectionListenerConnect(ConnectionStatusEnum connection) {
		for (ConnectionListener connectionListener : connectionListenerList) {
			try {
				connectionListener.onConnect(connection);
			} catch (Exception e) {
				log.severe(Log.Session.Monitor, "ConnectionListener raise an error [" + e.getMessage() +"]");
			}
		}
	}

	/**
	 * Close the socket and stop the thread.
	 */
	@Override
	public void close(){
		tryToConnect = false; // get out of the thread by stopping try to reconnect the monitor.
		resetSocket();
	}

	/**
	 * Reset the socket: thread (OpenWebMonitorImpl is a thread) will reopen the socket as soon as possible.
	 */
	private void resetSocket() {
		if(socket != null){
			try {
				for (ConnectionListener connectionListener : connectionListenerList) {
					try {
						connectionListener.onClose();
					} catch (Exception e) {
						log.severe(Log.Session.Monitor, "ConnectionListener raise an error [" + e.getMessage() +"]");
					}
				}
				socket.close();
				socket = null;
				log.fine(Log.Session.Monitor, "Close Monitor Session...");
			} catch (IOException e) {
				log.severe(Log.Session.Monitor, "Error during closing socket [" + e.getMessage() + "]");
			}
		}
	}

	private void write(String msg) {
		versClient.print(msg);
		versClient.flush();
		log.fine(Log.Session.Monitor, "TO   MONITOR SERVER: " + msg);
	}

	private String read(){
		int indice = 0;
		boolean exit = false;
		char respond[] = new char[1024];
		char c = ' ';
		int ci = 0;
		String responseString = null;

		try{
			do { 
				if(socket != null && !socket.isInputShutdown()) {
					ci = depuisClient.read();		    		
					if (ci == -1) {
						log.finest(Log.Session.Monitor, "End of read from monitor socket.");
						resetSocket();
						break;
					} else { 
						c = (char) ci;			        				        
						if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
							respond[indice] = c;
							exit = true;
							log.finest(Log.Session.Monitor, "End of message from monitor socket [" + new String(respond) + "].");
							break;
						} else {
							respond[indice] = c;
							indice = indice + 1;
						}
					}
				} else {
					resetSocket();
					break;
				}
			} while(true);
		}catch(IOException e){
			log.severe(Log.Session.Monitor, "Socket not available");
		}

		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}

		log.fine(Log.Session.Monitor, "FROM MONITOR SERVER: " + responseString);

		return responseString;
	}

	@Override
	public boolean isConnected() {
		if(socket != null){
			if (socket.isConnected()) {
				return true;		
			} else {
				resetSocket();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void addConnectionListener(
			ConnectionListener connectionListener) {
		connectionListenerList.add(connectionListener);
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public void addControllerStatusListener(CommandListener commandListener) {
		commandListenerList.add(commandListener);
	}

	@Override
	public void addUnknownControllerListener(
			UnknownControllerListener unknownControllerListener) {
		synchronized (unknownControllerListenerList) {
			unknownControllerListenerList.add(unknownControllerListener);
		}
		
	}
}
