package com.adgsoftware.mydomo.server.commandmodules;

import java.util.Hashtable;

public interface ControllerCommand {

	public String execute(String command, Hashtable<String, String> statusList);
	public String status(String command, Hashtable<String, String> statusList);
	public String getWho();
}