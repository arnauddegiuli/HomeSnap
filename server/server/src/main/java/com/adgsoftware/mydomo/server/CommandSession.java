package com.adgsoftware.mydomo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.adgsoftware.mydomo.engine.Command;

public class CommandSession implements Runnable {
	Socket client; // liaison avec client
	BufferedReader depuisClient; // réception de requête
	PrintWriter versClient; // envoi des réponses

	public CommandSession(Socket client, BufferedReader depuisClient, PrintWriter versClient) {
		this.client = client;
		this.depuisClient = depuisClient;
		this.versClient = versClient;
	}

	private void write(String msg) {
		versClient.print(msg);
		versClient.flush();
		System.out.println("COMMAND SERVER WRITE:[" + msg + "]");
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
//		    			System.out.println("End of read from socket.");
//			  			client = null;
//			  			break;
			        } else { 
			        	c = (char) ci;			        				        
					    if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
					    	respond[indice] = c;
					    	exit = true;
					    	break;
					    } else {
					    	respond[indice] = c;
					    	indice = indice + 1;
					    } 
			        }
	    		}
	        } while(true); 
		}catch(IOException e){
			System.out.println("Socket not available");
	    }
		
		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}
		
		System.out.println("COMMAND CLIENT WRITE: " + responseString);
		
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
					if (!Command.NACK.equals(result)) {
						// TODO call MonitorSession (by listener way?)
					}
				} else {
					result = ControllerStateManagement.executeStatus(lue);
				}					
				write(result);
			}
		}
		// fermeture de la connexion
		stop();
	}

	public void stop() {
		try {
			client.close();
		} catch (IOException e) {
			System.out.println("Exception à la fermeture d'une connexion : "
					+ e);
		}
	}
}
