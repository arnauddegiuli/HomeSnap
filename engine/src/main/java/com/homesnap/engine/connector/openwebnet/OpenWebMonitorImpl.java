package com.homesnap.engine.connector.openwebnet;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.ConnectionListener;
import com.homesnap.engine.connector.ConnectionStatusEnum;
import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.connector.UnknownControllerListener;
import com.homesnap.engine.connector.openwebnet.convert.OpenWebNetCommand;
import com.homesnap.engine.connector.openwebnet.convert.UnknownState;
import com.homesnap.engine.connector.openwebnet.convert.UnknownWho;
import com.homesnap.engine.connector.openwebnet.parser.ParseException;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.house.Label;

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
	private List<Controller> controllerList = new ArrayList<Controller>();
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

	/**
	 * Return the ip of the open server.
	 * @return the ip of the open server.
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Define the ip of the open server.
	 * @param ip the ip of the open server.
	 */
	public void setIp(String ip) {
		this.ip = ip;
		resetSocket();
	}

	/**
	 * Return the port of the open server.
	 * @return the port of the open server.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Define the port of the open server.
	 * @param port the port of the open server.
	 */
	public void setPort(int port) {
		this.port = port;
		resetSocket();
	}

	/**
	 * Define the gateway password.
	 * @param passwordOpen
	 */
	public void setPasswordOpen(Integer passwordOpen) {
		this.passwordOpen = passwordOpen;
	}

	@Override
	public void addControllerToMonitor(Controller controller) {
		controllerList.add(controller);
	}
	
	protected void onMessageReceipt(String message) {
		try {
			log.log(Session.Monitor, Level.FINE, "Message receiver ["+ message +"]");
			OpenWebNetCommand command = new OpenWebNetCommand(message);
			if (!(command.isDimensionCommand() || command.isStandardCommand())) {
				log.log(Session.Monitor, Level.SEVERE, "Message received [" + message +"] don't match with standard command or dimension command.");
				return;
			}

			boolean known = false;
			// TODO manage message on other bus
			if (command.isGeneralCommand()) {
				// We send command to all correct address
				for (int i = 11; i < 100; i++) {
					if (i % 10 != 0) { // group address (20, 30, ..) are not correct
						known |= updateController(new Where(""+i, ""+i), command);
					}
				}
			} else if (command.isGroupCommand()) {
				// We send command to group address
				for (Controller controller : controllerList) {
					for (Label label : controller.getLabels()) {
						if (label.getId().equals(command.getGroup()) &&
								controller.getWho().equals(command.getWho())) {
							known = true;
							if (command.isStandardCommand()) {
								for(What what : command.getWhat(controller.getStateMap())) {
									controller.receivedAction(what);
								}
							} else {
								for(What what : command.getDimension(controller.getStateMap())) {
									controller.receivedAction(what);
								}
							}
						}
					}
				}
			} else if (command.isEnvironmentCommand()) {
				String environment = command.getEnvironment();
				// We send ambiance command to address
				for (int i = 1; i < 10; i++) {
					known |= updateController(new Where(environment + i, environment + i), command);
				}
			} else {
				// Command direct on a controller
				known = updateController(command.getWhere(), command);
			}

			if (!known) {
				// Detected unknown device
				synchronized (unknownControllerListenerList) {
					for (UnknownControllerListener listener : unknownControllerListenerList) {
						if (command.isStandardCommand()) {
							listener.foundUnknownController(command.getWho(), command.getWhere(), command.getWhat(null));
						} else {
							listener.foundUnknownController(command.getWho(), command.getWhere(), command.getDimension(null));
						} 
					
					}
				}
			}
		} catch (ParseException e) {
			log.log(Session.Monitor, Level.WARNING, "Unknown message received [" + message +"]. Message dropped.");
		} catch (UnknownState e) {
			log.log(Session.Monitor, Level.WARNING, "Unknown state received [" + message +"]. Message dropped.");
		} catch (UnknownWho e) {
			log.log(Session.Monitor, Level.WARNING, "Unknown who received [" + message +"]. Message dropped.");
		}
	}

	private boolean updateController(Where where, OpenWebNetCommand command) throws UnknownState, UnknownWho {
		boolean known = false;

		// Manage what command
		for (Controller controller : controllerList) {
			if (command.getWho().equals(controller.getWho()) && where.getTo().equals(controller.getWhere().getTo())) {
				known = true;
				if (command.isStandardCommand()) {
					for(What what : command.getWhat(controller.getStateMap())) {
						controller.receivedAction(what);
					}
				} else {
					for(What what : command.getDimension(controller.getStateMap())) {
						controller.receivedAction(what);
					}
				}
				break;
			}
		}
		return known;
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
				String msg = read();
				if (!OpenWebNetConstant.ACK.equals(msg)) {
					// Bad return message
					log.severe(Log.Session.Monitor, "Bad message [" + msg + "] received from [" + ip + "]");
					callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
					this.resetSocket();
					return false;
				}

				log.finest(Log.Session.Monitor, "----- Step Identification -----");
				write(OpenWebNetConstant.MONITOR_SESSION);

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

				if (!OpenWebNetConstant.ACK.equals(msg)) {		       	
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
	public void disconnect(){
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

	/**
	 * Return the timeout of the connection to open server in millisecond.
	 * @return the timeout of the connection to open server in millisecond.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Define the timeout of the connection to open server in millisecond.
	 * @param timeout the timeout of the connection to open server in millisecond.
	 */
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

	@Override
	public void removeControllerToMonitor(
			Controller controller) {
		controllerList.remove(controller);
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		connectionListenerList.remove(connectionListener);
	}

	@Override
	public void removeControllerStatusListener(CommandListener commandListener) {
		commandListenerList.remove(commandListener);
	}

	@Override
	public void removeUnknownControllerListener(
			UnknownControllerListener unknownControllerListener) {
		unknownControllerListenerList.remove(unknownControllerListener);
	}
}
