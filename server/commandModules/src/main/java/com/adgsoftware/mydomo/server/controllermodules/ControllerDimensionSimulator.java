package com.adgsoftware.mydomo.server.controllermodules;


public interface ControllerDimensionSimulator {

	public String execute(String command);
	
	public String status(String command);
	
	public String getWho();
}