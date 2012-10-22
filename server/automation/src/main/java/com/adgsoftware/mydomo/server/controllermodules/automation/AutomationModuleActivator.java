package com.adgsoftware.mydomo.server.controllermodules.automation;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class AutomationModuleActivator implements BundleActivator {

	private AutomationSimulator automationModule = new AutomationSimulator();
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put("osgi.command.scope", "automation");
		dict.put("osgi.command.function", AutomationCommand.functions);

		
		context.registerService(AutomationCommand.class.getName(), new AutomationCommand(), dict);
		
		
		ControllerStateManagement.registerControllerCommand(automationModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.unRegisterControllerCommand(automationModule);
	}
	
	

}
