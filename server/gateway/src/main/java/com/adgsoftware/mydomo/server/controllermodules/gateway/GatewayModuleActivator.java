package com.adgsoftware.mydomo.server.controllermodules.gateway;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class GatewayModuleActivator implements BundleActivator {

	private GatewaySimulator gatewayModule = new GatewaySimulator();
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		Dictionary<String, Object> dict = new Hashtable<String, Object>();
		dict.put("osgi.command.scope", "gateway");
		dict.put("osgi.command.function", GatewayCommand.functions);

		
		context.registerService(GatewayCommand.class.getName(), new GatewayCommand(), dict);
		
		ControllerStateManagement.registerControllerDimensionCommand(gatewayModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerDimensionCommand(gatewayModule);
	}
	
	

}
