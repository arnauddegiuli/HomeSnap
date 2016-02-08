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
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.ConnectionListener;
import com.homesnap.engine.connector.openwebnet.convert.HomeSnapCommand;
import com.homesnap.engine.controller.Controller;

public class OpenWebCommanderImpl implements Commander {

	Socket socket;
	Log log = new Log();
	BufferedReader input = null;
	PrintWriter output = null;
	private String ip;
	private int port;
	private int timeout = 5000;
	private Integer passwordOpen;
	List<ConnectionListener> connectionListenerList = new ArrayList<ConnectionListener>();

	/**
	 * 
	 * @param ip the ip or dns name of the open server
	 * @param port the port number of the open server
	 * @param passwordOpen 
	 */
	public OpenWebCommanderImpl(String ip, int port, Integer passwordOpen) {
		this.ip = ip;
		this.port = port;
		this.passwordOpen = passwordOpen;
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
		disconnect();
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
		disconnect();
	}

	/**
	 * Asynchrone connection
	 */
	public boolean connect() { 
		// TODO manage if we start another thread by call connect again...
		new Thread(new OpenWebConnectThread(this)).start(); // Make connection in thread to avoid blocking the user!
		return false;
	}

	@Override
	public void disconnect() {
		if(socket != null){
			try {
				socket.close();
				socket = null;
				for (ConnectionListener connectionListener : connectionListenerList) {
					try {
						connectionListener.onClose();
					} catch (Exception e) {
						log.severe(Log.Session.Command, "ConnectionListener raise an error: " + e.getMessage());
					}
				}
				log.fine(Log.Session.Command, "Close Command Session...");
			} catch (IOException e) {				
				log.severe(Log.Session.Command, "Error during closing the socket: " + e.getMessage());
			}
		}
	}

	@Override
	public void sendCommand(Command command, CommandListener resultListener){
		log.fine(Log.Session.Command, "Prepare to send command");
		List<String> homeSnapCommand = new HomeSnapCommand(command).getCommand();
		if (homeSnapCommand != null) {
			for (String c : homeSnapCommand) {
				log.fine(Log.Session.Command, "Begin to send command: " + c);
				sendCommand(c, resultListener);
			}
		} else {
			log.fine(Log.Session.Command, "No translation command for : " + command.toString());
		}
		log.fine(Log.Session.Command, "End to send command: " + command.toString());
	}

	public void sendCommand(String command,  CommandListener resultListener) {
		synchronized (this) {
			if (!isConnected()) { // If socket close? => init connection.
				new Thread(new OpenWebConnectThread(this)).start(); // Open connection in thread to avoid blocking user!
			}
			// Wait to be sure that thread is run before the next => else sometime connection was started after command...
			try {
				this.wait(20);
			} catch (InterruptedException e) {
				log.severe(Session.Command, "Error during waiting 20 millisecond for connection thread.");
			}
		}	

		// Send asynchronously the command!
		new Thread(new OpenWebCommandThread(this, command, resultListener)).start();		
	}
	void writeMessage(String message) {
		if (output != null) { // No output can mean no server is responding
			output.write(message);
			output.flush();
			log.fine(Log.Session.Command, "TO   COMMAND SERVER: " + message);
		}
	}

	String readMessage(){
		int indice = 0;
		boolean exit = false;
		char respond[] = new char[1024];
		char c = ' ';
		int ci = 0;
		String responseString = null;

		try{
			do { 
				if(socket != null && !socket.isInputShutdown()) {
					ci = input.read();
					if (ci == -1) {
						log.finest(Log.Session.Command, "End of read from command server socket.");
						disconnect();
						break;
					} else { 
						c = (char) ci;
						if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
							respond[indice] = c;
							exit = true;
							log.finest(Log.Session.Command, "End of message from command server socket [" + new String(respond) + "].");
							break;
						} else {
							respond[indice] = c;
							indice = indice + 1;
						}
					}
				} else {
					disconnect();
					break;
				}
			} while(true); 
		}catch(IOException e){
			log.severe(Log.Session.Command, "Socket not available: " + e.getMessage());
		}

		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}

		log.fine(Log.Session.Command, "FROM COMMAND SERVER: " + responseString);

		return responseString;
	}

	@Override
	public void addControllerToExecute(Controller controller) {
		controller.setServer(this);
	}

	@Override
	public boolean isConnected() {
		if(socket != null){
			if (socket.isConnected()) {
				return true;		
			} else {
				disconnect();
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

	/**
	 * Define the gateway password
	 * @param password
	 */
	public void setPasswordOpen(Integer password) {
		this.passwordOpen = password;
	}

	public Integer getPasswordOpen() {
		return passwordOpen;
	}

	@Override
	public void removeControllerToExecute(
			Controller controller) {
		controller.setServer(null);
		
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		connectionListenerList.remove(connectionListener);
	}
}
