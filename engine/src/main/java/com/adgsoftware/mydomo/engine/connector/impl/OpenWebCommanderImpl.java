package com.adgsoftware.mydomo.engine.connector.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.Commander;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class OpenWebCommanderImpl implements Commander {
	
	Socket socket;
	Logger log = Logger.getLogger(OpenWebCommanderImpl.class.getName());
	BufferedReader input = null;
	PrintWriter output = null;
	private String ip;
	private int port;
	private int timeout = 5000;
//	private long passwordOpen;
	boolean usePassword = false; // TODO manage password
	List<ConnectionListener> connectionListenerList = new ArrayList<ConnectionListener>();
	
	
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
	
	/**
	 * Asynchrone connection
	 */
	public void connect() { 
		new Thread(new OpenWebConnectThread(this, null, null)).start(); // Make connection in thread to avoid blocking the user!
		
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
						log.log(Level.SEVERE, "ConnectionListener raise an error", e);
					}
				}
						
			} catch (IOException e) {				
				log.log(Level.SEVERE, "Error during closing the socket", e);
			}
		}
	}
	
	@Override
	public void sendCommand(String command, CommandListener resultListener){

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
			new Thread(new OpenWebConnectThread(this, command, resultListener)).start(); // Make connection and lunch command in thread to avoid block the user!
		} else { // FIXME here too lunch the command in a thread! => refactor code here
			log.finest("Tx: " + command);
			writeMessage(command);
			
			String msg = readMessage();
			
			log.finest("Rx: " + msg);
			
	    	if(msg == null){
	    		log.finest("Command failed.");
	    		if (resultListener != null) {
	    			resultListener.onCommand(new CommandResult(null, CommandResultStatus.error));
	    		}
	    		return;
	    	}
	
	    	if (Command.ACK.equals(msg)){
	    		log.finest("Command sent.");
	    		if (resultListener != null) {
	    			resultListener.onCommand(new CommandResult(Command.ACK, CommandResultStatus.ok));
	    		}
	    		return;
	    	} else if (Command.NACK.equals(msg)){
	    		log.finest("Command failed.");
	    		if (resultListener != null) {
	    			resultListener.onCommand(new CommandResult(Command.NACK, CommandResultStatus.nok));
	    		}
	    		return;
	    	} else { // First return was information. The next should be acknowledgment
	    		String actionReturn = msg;
	    		msg = readMessage();
	    		log.finest("Rx: " + msg);
	    		if(Command.ACK.equals(msg)){
	    			log.finest("Command sent.");
	    			if (resultListener != null) {
	    				resultListener.onCommand(new CommandResult(actionReturn, CommandResultStatus.ok));
	    			}
	    			return;
	    		} 
	
				log.finest("Command failed.");
				if (resultListener != null) {
					resultListener.onCommand(new CommandResult(actionReturn, CommandResultStatus.error));
				}
				return;
	    	}
		} 
	}

	void writeMessage(String message) {
		if (output != null) { // No output can mean no server is responding
			output.write(message);
			output.flush();
			System.out.println("CLIENT OUT: " + message);
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
}
