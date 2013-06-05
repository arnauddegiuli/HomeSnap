package com.adgsoftware.mydomo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;

public class CommandSession implements Runnable {
	private Socket client; // liaison avec client
	private BufferedReader depuisClient; // réception de requête
	private PrintWriter versClient; // envoi des réponses
	private Log log = new Log();
	
	public CommandSession(Socket client, BufferedReader depuisClient, PrintWriter versClient) {
		this.client = client;
		this.depuisClient = depuisClient;
		this.versClient = versClient;
	}

	private void write(String msg) {
		versClient.print(msg);
		versClient.flush();
		log.fine(Session.Command, "FROM COMMAND SERVER: " + msg);
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
	    		if(client != null && !client.isInputShutdown()) {
	    			ci = depuisClient.read();		    		
		    		if (ci == -1) {
		    			log.finest(Session.Command, "End of read from command client socket.");
			  			client = null;
			  			break;
			        } else { 
			        	c = (char) ci;			        				        
					    if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
					    	respond[indice] = c;
					    	exit = true;
					    	log.finest(Log.Session.Command, "End of message from command client socket [" + new String(respond) + "].");
					    	break;
					    } else {
					    	respond[indice] = c;
					    	indice = indice + 1;
					    } 
			        }
	    		} else {
	    			stop();
	    			break;
	    		}
	        } while(true); 
		}catch(IOException e){
			log.severe(Session.Command, "Socket not available");
	    }
		
		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}
		
		log.fine(Log.Session.Command, "FROM COMMAND CLIENT: " + responseString);
		
		return responseString;
    }
	
	public void run() {
		boolean fini = false; // drapeau
		String lue; // la requête
		while (!fini) {
			lue = read();
			if (lue == null) {
				fini = true;
			}
			else {
				String result = Command.NACK;
				
				if (Command.isStandardCommand(lue) || Command.isDimensionCommand(lue)) {
					result = ControllerStateManagement.executeCommand(lue);
				} else {
					result = ControllerStateManagement.executeStatus(lue);
				}					
				write(result);
			}
		}
		stop();
	}

	public void stop() {
		try {
			log.fine(Log.Session.Command, "End Command Session.");
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			log.severe(Session.Command, "Exception à la fermeture d'une connexion : "
					+ e.getMessage());
		}
		
		
	}
}
