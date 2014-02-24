package com.homesnap.engine;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Since we use the engine on android or other platform just use a little internal log system...
 * Sorry, it is faster than adapt log4j, slf4j, jdklogger,....
 * @author arnaud
 *
 */
public class Log {

	private boolean finest = false;
	private boolean debug = true;
	private boolean error = true;
	private boolean info = true;
	private boolean logMonitor = true;
	private boolean logCommand = true;

	public enum Session {
		Monitor, Command, Server;
	}

	public void log(Session session, Level level, String log) {
		
		if (session.equals(Log.Session.Monitor) && !logMonitor) {
			return; // not log when log monitor is false;
		}
		
		if (session.equals(Log.Session.Command) && !logCommand) {
			return; // not log when log command is false;
		}
		
		if (debug && level.intValue()>Level.ALL.intValue() ||
				info && level.intValue()>Level.INFO.intValue() ||
				error && level.intValue()>Level.SEVERE.intValue()) {

			String s = session.name().toUpperCase();
			System.out.println("[" + getTime() + "] -" + s + " SESSION- " + log);
		}
	}
	
	public void finest(Session session, String log) {
		if (finest) {
			log(session, Level.FINEST, log);
		}
	}

	public void fine(Session session, String log) {
		if (debug) {
			log(session, Level.FINE, log);
		}
	}

	public void severe(Session session, String log) {
		if (error) {
			log(session, Level.SEVERE, log);
		}
	}

	private String getTime() {
		return new SimpleDateFormat("hh:mm:ss:SSSS").format(new Date());
	}
}
