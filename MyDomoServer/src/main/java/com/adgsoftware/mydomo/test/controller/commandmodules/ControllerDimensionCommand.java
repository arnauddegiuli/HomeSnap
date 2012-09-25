package com.adgsoftware.mydomo.test.controller.commandmodules;

import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.controller.DimensionValue;

public interface ControllerDimensionCommand {

	public String execute(String command,
			Hashtable<String, List<DimensionValue>> dimensionCache);
	
	public String status(String command,
			Hashtable<String, List<DimensionValue>> dimensionCache);
	
	public String getWho();
}