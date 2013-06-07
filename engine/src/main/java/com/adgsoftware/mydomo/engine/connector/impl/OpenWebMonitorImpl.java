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

	Log log = new Log();
//	GestionePassword gestPassword = null;
	
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
	
	/**
	 * 
	 * @param ip the ip or dns name of the open server
	 * @param port the port number of the open server
	 * @param passwordOpen not supported actually
	 */
	public OpenWebMonitorImpl(String ip, int port, long passwordOpen){ 
		this.ip = ip;
		this.port = port;
//		this.passwordOpen = passwordOpen;
		this.start();
	}
	
	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public void setIp(String ip) {
		this.ip = ip;
		close();
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
		close();
	}

	@Override
	public void addControllerToMonitor(Controller<? extends Status> controller) {
		controllerList.add(controller);
	}
	
	protected void onMessageReceipt(String message) {
		String where = Command.getWhereFromCommand(message);
		String what = Command.getWhatFromCommand(message);
		boolean known = false;
		if (what != null) {
			// Manage what command
			for (Controller<? extends Status> controller : controllerList) {
				if (controller.getWhere().equals(where)) {
					known = true;
					changeWhat(controller, what);
				}
			}
		} else {
			// Manage dimension command
			List<DimensionValue> dimensionList = Command.getDimensionListFromCommand(message);
			String code = Command.getDimensionFromCommand(message);
			for (Controller<? extends Status> controller : controllerList) {
				if (controller.getWhere().equals(where)) {
					known = true;
					if (controller instanceof ControllerDimension<?>){
						changeDimension((ControllerDimension<? extends Status>) controller, code, dimensionList);// TODO: le changeWhat relance l'action.... => pas bon!
					}
					else {
						// error!!!
					}
				}
			}
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
		} while (true);
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
				this.close();
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
                    this.close();
                    return false;
    			}
	            
                log.finest(Log.Session.Monitor, "----- Step Identification -----");
            	write(Command.MONITOR_SESSION);

//    			if(usePassword){
//    				msg = read();
//    				log.finest("\n----- Step authentification -----");
//    	            log.finest("Rx: " + msg);
//    	            
//    		    	long password = 0; //gestPassword.applicaAlgoritmo(passwordOpen, msg); TODO manage password
//    		    	String passwordMsg = "*#"+password+"##"; 
//    		    	log.finest("Tx: " + passwordMsg);
//    		    	write(passwordMsg);		    	
//            	}
            	log.finest(Log.Session.Monitor, "----- Step Final -----");
            	msg = read();
    	    	
    			if (!Command.ACK.equals(msg)) {		       	
    		        log.severe(Log.Session.Monitor, "Problem during connection to [" + ip + "] with message [" + msg + "]");
    		        callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
    		        this.close();
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
	
	@Override
	public void close(){
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
			  			close();
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
	    			close();
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
