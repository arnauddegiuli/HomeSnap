package com.homesnap.engine.connector.openwebnet;

/*
 * #%L
 * MyDomoEngine
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

import com.homesnap.engine.Log;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.controller.CommandListener;

/**
 * Manage the connection in a new thread to be asynchrone and avoid blocking process. 
 *
 */
public class OpenWebCommandThread implements Runnable {

	Log log = new Log();
	private OpenWebCommanderImpl commander;
	private String command;
	private CommandListener resultListener;

	public OpenWebCommandThread(OpenWebCommanderImpl commander, String command, CommandListener resultListener) {
		this.commander = commander;
		this.command = command;
		this.resultListener = resultListener;
	}

	public void run() {
		sendCommand(command, resultListener);
	}

	public void stop() {
	}

	public void sendCommand(String command, CommandListener resultListener) { 
		synchronized (commander) { // mutex on the main thread: only one connection or send message at the same time!
			if (commander.isConnected()) { // Test again since with the lock, maybe a previous thread has closed the connection!
				commander.writeMessage(command);
				String msg = commander.readMessage();

				if(msg == null){
					log.severe(Log.Session.Command, "Command failed.");
					if (resultListener != null) {
						resultListener.onCommand(new OpenWebNetCommandResult(null, CommandResultStatus.error));
					}
					return;
				}

				if (OpenWebNetConstant.ACK.equals(msg)){
					log.finest(Log.Session.Command, "Command sent.");
					if (resultListener != null) {
						resultListener.onCommand(new OpenWebNetCommandResult(OpenWebNetConstant.ACK, CommandResultStatus.ok));
					}
					return;
				} else if (OpenWebNetConstant.NACK.equals(msg)){
					log.severe(Log.Session.Command, "Command failed.");
					if (resultListener != null) {
						resultListener.onCommand(new OpenWebNetCommandResult(OpenWebNetConstant.NACK, CommandResultStatus.nok));
					}
					return;
				} else { // First return was information. The next should be acknowledgment
					String actionReturn = msg;
					msg = commander.readMessage();
					if(OpenWebNetConstant.ACK.equals(msg)){
						log.finest(Log.Session.Command, "Command sent.");
						if (resultListener != null) {
							resultListener.onCommand(new OpenWebNetCommandResult(actionReturn, CommandResultStatus.ok));
						}
						return;
					}

					log.severe(Log.Session.Command, "Command failed.");
					if (resultListener != null) {
						resultListener.onCommand(new OpenWebNetCommandResult(actionReturn, CommandResultStatus.error));
					}
					return;
				}
			} else { // connection closed...
				log.severe(Log.Session.Command, "Command failed (Connection closed).");
				if (resultListener != null) {
					resultListener.onCommand(new OpenWebNetCommandResult(OpenWebNetConstant.NACK, CommandResultStatus.nok));
				}
				return;
			}
		}
	}
}
