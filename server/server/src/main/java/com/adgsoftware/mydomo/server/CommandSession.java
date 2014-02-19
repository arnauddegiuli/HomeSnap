package com.adgsoftware.mydomo.server;

/*
 * #%L
 * MyDomoServer
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

import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;
import com.adgsoftware.mydomo.engine.connector.openwebnet.OpenWebNetCommand;
import com.adgsoftware.mydomo.engine.connector.openwebnet.OpenWebNetConstant;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;

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
		} catch(IOException e) {
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
				String result = OpenWebNetConstant.NACK;
				try {
					OpenWebNetCommand parser = new OpenWebNetCommand(lue);
					if (parser.isStandardCommand() || parser.isDimensionCommand()) {
						result = ControllerStateManagement.executeCommand(lue);
					} else {
						result = ControllerStateManagement.executeStatus(lue);
					}	
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
