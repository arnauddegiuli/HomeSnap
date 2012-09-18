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
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.connector.ConnectionStatusEnum;
import com.adgsoftware.mydomo.engine.connector.OpenWebCommander;
import com.adgsoftware.mydomo.engine.connector.OpenWebConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class OpenWebCommanderImpl implements OpenWebCommander {
	
	private Socket socket;
	Logger log = Logger.getLogger(OpenWebCommanderImpl.class.getName());
	private BufferedReader input = null;
	private PrintWriter output = null;
	private String ip;
	private int port;
	private int timeout = 5000;
//	private long passwordOpen;
	private boolean usePassword = false; // TODO manage password
	private List<OpenWebConnectionListener> connectionListenerList = new ArrayList<OpenWebConnectionListener>();
	
	
	/**
	 * 
	 * @param ip the ip or dns name of the open server
	 * @param port the port number of the open server
	 * @param passwordOpen 
	 */
	public OpenWebCommanderImpl(String ip, int port, long passwordOpen) {
		this.ip = ip;
		this.port = port;
//		this.passwordOpen = passwordOpen;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		close();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		close();
	}
	
	/* (non-Javadoc)
	 * @see com.adgsoftware.mydomo.engine.connector.OpenWebCommand#connect(java.lang.String, int, long)
	 */
	@Override
	public boolean connect() { 
		try{
			log.finest("Connect to the web open server socket ["+ ip +":"+ port+"]");
			if (ip == null || port == 0) {
				return false;
			}
			socket = new Socket();
			InetSocketAddress address = new InetSocketAddress(ip, port);
			socket.connect(address, getTimeout());
			input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			log.finest("InputReader created.");
			output = new PrintWriter(socket.getOutputStream(),true);
			log.finest("PrintWriter created.");
		} catch (Exception e){
			log.log(Level.FINER, "Impossible to connect to the server ["+ ip +":"+ port+"]", e);
			if (socket!=null && socket.isConnected()) {
				this.close();
			}
			return false;
		}
		
		if(socket != null) {
				
			String msg = readMessage();
				
        	log.finest(" ----- Step Connection ----- ");
        	log.finest("Rx: " + msg);
            if (!Command.ACK.equals(msg)) {
            	// Bad return message
                log.log(Level.SEVERE, "Bad message [" + msg + "] received from [" + ip + "]");
                callOpenWebConnectionListenerConnect(ConnectionStatusEnum.WrongAcknowledgement);
                this.close();
                return false;
            }
			
            log.finest("\n----- Step Identification -----");
        	log.finest("Tx: " + Command.COMMAND_SESSION);
        	writeMessage(Command.COMMAND_SESSION);
            
			if(usePassword){
				msg = readMessage();
				log.finest("\n----- Step authentification -----");
	            log.finest("Rx: " + msg);
	            
		    	long password = 0; //gestPassword.applicaAlgoritmo(passwordOpen, msg); TODO manage password
		    	String passwordMsg = "*#"+password+"##"; 
		    	log.finest("Tx: " + passwordMsg);
		    	writeMessage(passwordMsg);		    	
        	} 
			
	    	msg = readMessage();
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
		for (OpenWebConnectionListener connectionListener : connectionListenerList) {
			try {
				connectionListener.onConnect(connection);
			} catch (Exception e) {
				log.log(Level.SEVERE, "ConnectionListener raise an error", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.adgsoftware.mydomo.engine.connector.OpenWebCommand#close()
	 */
	@Override
	public void close() {
		if(socket != null){
			try {
				socket.close();
				socket = null;
				for (OpenWebConnectionListener connectionListener : connectionListenerList) {
					try {
						connectionListener.onClose();
					} catch (Exception e) {
						log.log(Level.SEVERE, "ConnectionListener raise an error", e);
					}
				}
						
			} catch (IOException e) {				
				log.log(Level.SEVERE, "Error during closing the socket", e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.adgsoftware.mydomo.engine.connector.OpenWebCommand#sendCommand(java.lang.String)
	 */
	@Override
	public CommandResult sendCommand(String command){

//		try { TODO check command!!!!
//			openWebNet = new OpenWebNet(comandoOpen);
//			if(openWebNet.isErrorFrame()){
//				ClientFrame.scriviSulLog("ERRATA frame open "+comandoOpen+", la invio comunque!!!",1,0,0);
//			}else{
//				ClientFrame.scriviSulLog("CREATO oggetto OpenWebNet "+openWebNet.getFrameOpen(),1,0,0);
//			}
//		}catch(Exception e){
//			ClientFrame.scriviSulLog("ERRORE nella creazione dell'oggetto OpenWebNet "+comandoOpen,1,0,0);
//			System.out.println("Eccezione in GestioneSocketComandi durante la creazione del'oggetto OpenWebNet");
//			e.printStackTrace();
//		}

		if (!isConnected()) {
			connect();
		}
		
		if (isConnected()) {
			log.finest("Tx: " + command);
			writeMessage(command);
			
			String msg = readMessage();
			
			log.finest("Rx: " + msg);
			
	    	if(msg == null){
	    		log.finest("Command failed.");
	    		return new CommandResult(null, CommandResultStatus.error);
	    	}
	
	    	if (Command.ACK.equals(msg)){
	    		log.finest("Command sent.");
	    		return new CommandResult(Command.ACK, CommandResultStatus.ok);
	    	} else if (Command.NACK.equals(msg)){
	    		log.finest("Command failed.");
	    		return new CommandResult(Command.NACK, CommandResultStatus.nok);
	    	} else { // First return was information. The next should be acknowledgment
	    		String actionReturn = msg;
	    		msg = readMessage();
	    		log.finest("Rx: " + msg);
	    		if(Command.ACK.equals(msg)){
	    			log.finest("Command sent.");
	    			return new CommandResult(actionReturn, CommandResultStatus.ok);
	    		} 
	
				log.finest("Command failed.");
	    		return new CommandResult(actionReturn, CommandResultStatus.error);
	    	}
		} else {
			return new CommandResult(null, CommandResultStatus.error);
		}
	}

	private void writeMessage(String message) {
		if (output != null) { // No output can mean no server is responding
			output.write(message);
			output.flush();
			System.out.println("CLIENT OUT: " + message);
		}
	}
	
    private String readMessage(){
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
			  			log.finest("End of read from socket.");
			  			socket = null;
			  			break;
			        } else { 
			        	c = (char) ci;			        				        
					    if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
					    	respond[indice] = c;
					    	exit = true;
					    	log.finest("End of message from socket [" + respond.toString() + "].");
					    	break;
					    } else {
					    	respond[indice] = c;
					    	indice = indice + 1;
					    } 
			        }
	    		}
	        } while(true); 
		}catch(IOException e){
			log.log(Level.SEVERE, "Socket not available", e);
	    }
		
		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}
		
		System.out.println("CLIENT IN: " + responseString);
		
		return responseString;
    }

	@Override
	public void addControllerToExecute(Controller<? extends Status> controller) {
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
			OpenWebConnectionListener connectionListener) {
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
}
