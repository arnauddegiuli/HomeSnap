package com.adgsoftware.mydomo.server.controllermodules.heating;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class HeatingModuleActivator implements BundleActivator {

	private HeatingSimulator heatingModule = new HeatingSimulator();
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put("osgi.command.scope", "heating");
		dict.put("osgi.command.function", HeatingCommand.functions);

		
		context.registerService(HeatingCommand.class.getName(), new HeatingCommand(), dict);
		
		ControllerStateManagement.registerControllerDimensionCommand(heatingModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerDimensionCommand(heatingModule);
	}
	
	

}
