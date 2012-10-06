package com.adgsoftware.mydomo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MonitorSession {
	Socket client; // liaison avec client
	BufferedReader depuisClient; // réception de requête
	PrintWriter versClient; // envoi des réponses

	public MonitorSession(Socket client, BufferedReader depuisClient, PrintWriter versClient) {
		this.client = client;
		this.depuisClient = depuisClient;
		this.versClient = versClient;
	}

	private void write(String msg) throws IOException {
		versClient.print(msg);
		if (versClient.checkError()) {
			System.out.println("ERROR WHEN MONITOR SERVER WRITE:[" + msg + "]");
			throw new IOException();
		} else {
			System.out.println("MONITOR SERVER WRITE:[" + msg + "]");
		}
	}

	public void monitor(String command) {
		try {
			write(command);
		} catch (IOException e) {
			System.out.println("Exception entrée/sortie : " + e.getMessage());
			stop();
		}
	}
	
	public void stop() {
		try {
			System.out.println("Fermeture d'une connexion");
			client.close();
			ControllerStateManagement.unRegisterMonitorSession(this);
		} catch (IOException e) {
			System.out.println("Exception à la fermeture d'une connexion : "
					+ e);
		}
	}
}
