package com.adgsoftware.mydomo.engine.connector.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionStatusEnum;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.Monitor;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.DimensionStatus;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.Status;

public class OpenWebMonitorImpl extends Thread 
implements Monitor {

//	GestionePassword gestPassword = null;
	Logger log = Logger.getLogger(OpenWebMonitorImpl.class.getName());
	
	private Socket socket = null;	
	private BufferedReader depuisClient = null;
	private PrintWriter versClient = null;
	private String ip;
	private int port;
	private int timeout = 5000;
	private List<ConnectionListener> connectionListenerList = new ArrayList<ConnectionListener>();
	private List<CommandListener> commandListenerList = new ArrayList<CommandListener>();
	
	
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
		if (what != null) {
			// Manage what command
			
			// TODO raise the event commandListenerList
			
			for (Controller<? extends Status> controller : controllerList) {
				if (controller.getWhere().equals(where)) {
					changeWhat(controller, what);
				}
			}
		} else {
			// Manage dimension command
			// TODO raise the event commandListenerList
			List<DimensionValue> dimensionList = Command.getDimensionListFromCommand(message);
			String code=null; // TODO get code from commande
			for (Controller<? extends Status> controller : controllerList) {
				if (controller.getWhere().equals(where)) {
					if (controller instanceof ControllerDimension<?>){
						changeDimension((ControllerDimension<? extends Status>) controller, code, dimensionList);// TODO: le changeWhat relance l'action.... => pas bon!
					}
					else {
						// error!!!
					}
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
		if (dimensionStatus.getValueList() != dimensionList) {
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
						log.finest("Timeout interrupted.");
					}
				}
			}
		} while (true);
	}
	
	@Override
	public boolean connect() {
		try {
			log.finest("Connect to the web server socket["+ ip +":"+ port+"]");
			socket = new Socket();
			InetSocketAddress address = new InetSocketAddress(ip, port);
			socket.connect(address, getTimeout());
			depuisClient= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			log.finest("InputReader created.");
			versClient = new PrintWriter(socket.getOutputStream(),true);
			log.finest("PrintWriter created.");
		}catch (Exception e){
			log.fine("Impossible to connect to the server ["+ip+":"+port+"]");
			if (socket!=null && socket.isConnected()) {
				this.close();
			}
			return false;
		}
		
		if(socket != null){
			
			String msg = read(); // TODO thread!
				
    			log.finest(" ----- Step Connection ----- ");
    			log.finest("Rx: " + msg);
    			if (!Command.ACK.equals(msg)) {
    				// Bad return message
                    log.fine("Bad message [" + msg + "] received from [" + ip + "]");
                    callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
                    this.close();
                    return false;
    			}
	            
                log.finest("\n----- Step Identification -----");
            	log.finest("Tx: " + Command.MONITOR_SESSION);
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
    	    	msg = read();
    	    	log.finest("\n----- Step Final -----");
    	    	log.finest("Rx: " + msg);
    			if (!Command.ACK.equals(msg)) {		       	
    		        log.finest("Problem during connection to [" + ip + "] with message [" + msg + "]");
    		        callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
    		        this.close();
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
	
	private void callOpenWebConnectionListenerConnect(ConnectionStatusEnum connection) {
		for (ConnectionListener connectionListener : connectionListenerList) {
			try {
				connectionListener.onConnect(connection);
			} catch (Exception e) {
				log.log(Level.SEVERE, "ConnectionListener raise an error", e);
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
						log.log(Level.SEVERE, "ConnectionListener raise an error", e);
					}
				}
				socket.close();
				socket = null;
			} catch (IOException e) {
				log.log(Level.FINE, "Error during closing socket", e);
			}
		}
	}
	
	private void write(String msg) {
		versClient.print(msg);
		versClient.flush();
		System.out.println("SERVER OUT:[" + msg + "]");
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
		    			System.out.println("End of read from socket.");
			  			close();
			  			break;
			        } else { 
			        	c = (char) ci;			        				        
					    if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
					    	respond[indice] = c;
					    	exit = true;
					    	System.out.println("End of message from socket [" + new String(respond) + "].");
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
			System.out.println("Socket not available");
	    }
		
		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}
		
		System.out.println("SERVER IN: " + responseString);
		
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
}
