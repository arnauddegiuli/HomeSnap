package com.homesnap.scanner;

/*
 * #%L
 * OpenWebNetScanner
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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
import java.net.InetSocketAddress;
import java.net.Socket;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;

public class PortScanner {

	private static Log log = new Log();
	
	private static Socket socket = null;	
	private static BufferedReader depuisClient = null;
	private static String ip = "192.168.1.35";
	
	public static void main(String[] args) {
		boolean find = false;
		int port = 444;
		
		while (!find || port > 5000) {
			find = connect(port);
			if (!find) {
				port++;
			}
		}
		
		log.severe(Session.Server, "Port:" + port);
	}
	
	private static boolean connect(int port){
	
	try {
		log.fine(Log.Session.Monitor, "Connect to socket ["+ ip +":"+ port+"]");
		socket = new Socket();
		InetSocketAddress address = new InetSocketAddress(ip, port);
		socket.connect(address, 1000);
		depuisClient= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		log.finest(Log.Session.Server, "InputReader created.");
	}catch (Exception e){
		log.severe(Log.Session.Server, "Impossible to connect to the server ["+ip+":"+port+"]");
		if (socket!=null && socket.isConnected()) {
			resetSocket();
		}
		return false;
	}

	if(socket != null){
		
			log.finest(Log.Session.Server, "----- Check connection ----- ");
			String msg = read();
			if (!OpenWebNetConstant.ACK.equals(msg)) {
				// Bad return message
				log.severe(Log.Session.Server, "Bad message [" + msg + "] received from [" + ip + "]");
				resetSocket();
				return false;
			} else {
				resetSocket();
				return true;
			}
		} else {
			log.severe(Log.Session.Monitor, "No socket... Impossible to connect");
			return false;
		}
	}
	
	/**
	 * Reset the socket: thread (OpenWebMonitorImpl is a thread) will reopen the socket as soon as possible.
	 */
	private static void resetSocket() {
		if(socket != null){
			try {
				socket.close();
				socket = null;
				log.fine(Log.Session.Server, "Close Socket.");
			} catch (IOException e) {
				log.severe(Log.Session.Server, "Error during closing socket [" + e.getMessage() + "]");
			}
		}
	}

	private static String read(){
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
						log.finest(Log.Session.Server, "End of read from monitor socket.");
						resetSocket();
						break;
					} else { 
						c = (char) ci;			        				        
						if (c == '#' && indice > 1 && '#' == respond[indice-1]) {
							respond[indice] = c;
							exit = true;
							log.finest(Log.Session.Server, "End of message from monitor socket [" + new String(respond) + "].");
							break;
						} else {
							respond[indice] = c;
							indice = indice + 1;
						}
					}
				} else {
					resetSocket();
					break;
				}
			} while(true);
		}catch(IOException e){
			log.severe(Log.Session.Server, "Socket not available");
		}

		if (exit == true){
			responseString = new String(respond,0,indice+1);
		}

		log.fine(Log.Session.Server, "READ: " + responseString);

		return responseString;
	}
}
