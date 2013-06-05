package com.adgsoftware.mydomo.engine;

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
