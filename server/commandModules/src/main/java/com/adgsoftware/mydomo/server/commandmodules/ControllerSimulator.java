package com.adgsoftware.mydomo.server.commandmodules;


public interface ControllerSimulator {

	public String execute(String command);
	public String status(String command);
	public String getWho();
}