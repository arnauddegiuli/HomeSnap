package com.adgsoftware.mydomo.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class MonitorSession implements Runnable {
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
	
	public void run() {
		boolean fini = false; // drapeau
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("MonitorDictionnary.properties"));

			while (!fini) {
				LineNumberReader ln = new LineNumberReader(new InputStreamReader(System.in));
				String s = ln.readLine();
				if ("exit".equalsIgnoreCase(s)) {
					fini = true;
					System.out.println("Exit!");
				} else {
					String result = p.getProperty(s);
						
					if (result != null) {
						write(result);
					} else {
						System.out.println("Command unknown [" + s + ":" + result +"]");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception entrée/sortie : " + e.getMessage());
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
