package com.adgsoftware.mydomo.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;

public class MonitorSession {
	private Socket client; // liaison avec client
	private PrintWriter versClient; // envoi des réponses
	private Log log = new Log();
	
	public MonitorSession(Socket client, PrintWriter versClient) {
		this.client = client;
		this.versClient = versClient;
	}

	private void write(String msg) throws IOException {
		versClient.print(msg);
		if (versClient.checkError()) {
			log.severe(Session.Monitor, "ERROR WHEN MONITOR SERVER WRITE: " + msg);
			throw new IOException();
		} else {
			log.fine(Session.Monitor, "MONITOR SERVER WRITE: " + msg);
		}
	}

	public void monitor(String command) {
		try {
			write(command);
		} catch (IOException e) {
			log.severe(Session.Monitor, "Error with IO : " + e.getMessage());
			stop();
		}
	}
	
	public void stop() {
		try {
			log.fine(Session.Monitor, "End Monitor Session.");
			client.close();
		} catch (IOException e) {
			log.severe(Session.Monitor, "Error when closing connection : "
					+ e.getMessage());
		} finally {
			ControllerStateManagement.unRegisterMonitorSession(this);
		}
	}
}
