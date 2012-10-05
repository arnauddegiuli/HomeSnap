package com.adgsoftware.mydomo.server.controllermodules.gateway;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class GatewayModuleActivator implements BundleActivator {

	private GatewaySimulator gatewayModule = new GatewaySimulator();
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerDimensionCommand(gatewayModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerDimensionCommand(gatewayModule);
	}
	
	

}
