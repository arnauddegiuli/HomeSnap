package com.adgsoftware.mydomo.server.commandmodules.light;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class LightModuleActivator implements BundleActivator {

	private LightSimulator lightModule = new LightSimulator();
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put("osgi.command.scope", "light");
		dict.put("osgi.command.function", LightCommand.functions);

		
		context.registerService(LightCommand.class.getName(), new LightCommand(), dict);
		
		
		ControllerStateManagement.registerControllerCommand(lightModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.unRegisterControllerCommand(lightModule);
	}
	
	

}
