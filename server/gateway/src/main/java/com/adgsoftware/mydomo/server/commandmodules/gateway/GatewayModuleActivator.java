package com.adgsoftware.mydomo.server.commandmodules.gateway;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class GatewayModuleActivator implements BundleActivator {

	private GatewayCommand gatewayModule = new GatewayCommand();
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerDimensionCommand(gatewayModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerDimensionCommand(gatewayModule);
	}
	
	

}
