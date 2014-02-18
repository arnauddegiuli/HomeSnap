package com.adgsoftware.mydomo.engine.connector.openwebnet;

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
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;
import com.adgsoftware.mydomo.engine.connector.Commander;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionStatus;
import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.Command;
import com.adgsoftware.mydomo.engine.controller.CommandListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.what.StateName;

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
		close();
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
		close();
	}

	/**
	 * Asynchrone connection
	 */
	public void connect() { 
		// TODO manage if we start another thread by call connect again...
		new Thread(new OpenWebConnectThread(this)).start(); // Make connection in thread to avoid blocking the user!
	}

	@Override
	public void close() {
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
		new Thread(new OpenWebCommandThread(this, createMessage(command), resultListener)).start();
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
						close();
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
					close();
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
				close();
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

	/**
	 * Create the open message for action or status.
	 * @return open web net message.
	 */
	public String createMessage(Command command) {
		if (command.getWhere() == null && command.getWhere().getTo() == null) {
			throw new IllegalArgumentException("Controller must contain an address with where");
		}
		
		String who = OpenWebNetWho.convert(command.getWho());
		String where = command.getWhere().getTo();
		if (command.isActionCommand()) {
			if (StateName.STATUS.equals(command.getWhat().getName())) {
				return MessageFormat.format(CommandConstant.COMMAND, new Object[] {who, StatusMapping.convert(command.getWho(), command.getWhat().getValue()), where});
			} else { // Dimension
				DimensionStatus dimensionStatus = DimensionMapping.convert(command.getWhat()); 
				StringBuilder sb = new StringBuilder();
				for (DimensionValue dimension : dimensionStatus.getValueList()) {
					sb.append(dimension.getValue());
					sb.append(CommandConstant.DIMENSION_SEPARATOR);
				}
				sb.setLength(sb.length()-1);
				
				return MessageFormat.format(CommandConstant.DIMENSION_COMMAND, new Object[] {who, where, dimensionStatus.getCode(), sb.toString()});
			}
		} else {
			if (StateName.STATUS.equals(command.getWhat().getName())) {
				return MessageFormat.format(CommandConstant.STATUS, new Object[] {who, where});
			} else {
				DimensionStatus dimensionStatus = DimensionMapping.convert(command.getWhat());
				return MessageFormat.format(CommandConstant.DIMENSION_STATUS, new Object[] {who, where, dimensionStatus.getCode()});
			}
		}
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
