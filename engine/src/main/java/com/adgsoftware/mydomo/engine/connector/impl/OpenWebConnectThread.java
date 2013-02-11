package com.adgsoftware.mydomo.engine.connector.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionStatusEnum;

/**
 * Manage the connection in a new thread to be asynchrone and avoid blocking process. 
 *
 */
public class OpenWebConnectThread implements Runnable {
	
	Logger log = Logger.getLogger(OpenWebConnectThread.class.getName());
	private OpenWebCommanderImpl commander;
	
	public OpenWebConnectThread(OpenWebCommanderImpl commander) {
		this.commander = commander;
	}

	public void run() {
		connect();
	}

	public void stop() {
	}
		
	public boolean connect() {
		synchronized (OpenWebCommanderImpl.class) { // mutex on the main thread: only one connection or send message at the same time!
			if (!commander.isConnected()) { // Test again since with the lock, maybe a previous thread has opened the connection!
				try {
					String ip = commander.getIp();
					int port = commander.getPort();
					
					log.finest("Connect to the web open server socket ["+ ip +":"+ port+"]");
					if (ip == null || port == 0) {
						return false;
					}
					
					commander.socket = new Socket();
					InetSocketAddress address = new InetSocketAddress(ip, port);
					commander.socket.connect(address, commander.getTimeout());
					commander.input= new BufferedReader(new InputStreamReader(commander.socket.getInputStream()));
					log.finest("InputReader created.");
					commander.output = new PrintWriter(commander.socket.getOutputStream(),true);
					log.finest("PrintWriter created.");
				} catch (Exception e){
					log.log(Level.FINER, "Impossible to connect to the server ["+ commander.getIp() +":"+ commander.getPort()+"]", e);
					if (commander.socket!=null && commander.socket.isConnected()) {
						commander.close();
					}
					return false;
				}
				
				if(commander.socket != null) {
						
					String msg = commander.readMessage();
						
		        	log.finest(" ----- Step Connection ----- ");
		        	log.finest("Rx: " + msg);
		            if (!Command.ACK.equals(msg)) {
		            	// Bad return message
		                log.log(Level.SEVERE, "Bad message [" + msg + "] received from [" + commander.getIp() + "]");
		                callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
		                commander.close();
		                return false;
		            }
					
		            log.finest("\n----- Step Identification -----");
		        	log.finest("Tx: " + Command.COMMAND_SESSION);
		        	commander.writeMessage(Command.COMMAND_SESSION);
		            
					if(commander.usePassword){
						msg = commander.readMessage();
						log.finest("\n----- Step authentification -----");
			            log.finest("Rx: " + msg);
			            
				    	long password = 0; //gestPassword.applicaAlgoritmo(passwordOpen, msg); TODO manage password
				    	String passwordMsg = "*#"+password+"##"; 
				    	log.finest("Tx: " + passwordMsg);
				    	commander.writeMessage(passwordMsg);		    	
		        	} 
					
			    	msg = commander.readMessage();
			    	log.finest("\n----- Step Final -----");
			    	log.finest("Rx: " + msg);
					if (!Command.ACK.equals(msg)) {		       	
				        log.finest("Problem during connection to [" + commander.getIp() + "] with message [" + msg + "]");
				        callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
				        commander.close();
				        return false;
				    }
					
					log.finest("Connection OK");
					callOpenWebConnectionListenerConnect(ConnectionStatusEnum.Connected);
			        return true;
		
				} else {
					log.finest("No socket... Impossible to connect");
					callOpenWebConnectionListenerConnect(ConnectionStatusEnum.NoSocket);
					return false;
				}
			}
			
			return true;
		}
	}
	
	private void callOpenWebConnectionListenerConnect(ConnectionStatusEnum connection) {
		for (ConnectionListener connectionListener : commander.connectionListenerList) {
			try {
				connectionListener.onConnect(connection);
			} catch (Exception e) {
				log.log(Level.SEVERE, "ConnectionListener raise an error", e);
			}
		}
	}
}
