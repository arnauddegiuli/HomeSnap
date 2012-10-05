package com.adgsoftware.mydomo.server.commandmodules;


public interface ControllerDimensionSimulator {

	public String execute(String command);
	
	public String status(String command);
	
	public String getWho();
}