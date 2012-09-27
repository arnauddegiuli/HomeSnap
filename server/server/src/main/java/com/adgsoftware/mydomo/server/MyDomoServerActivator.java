package com.adgsoftware.mydomo.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.commandmodules.gateway.GatewayCommand;
import com.adgsoftware.mydomo.server.commandmodules.light.LightCommand;

public class MyDomoServerActivator implements BundleActivator {

	private Server server;
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		server = new Server();
		
		// TODO remove this thing... later will be loaded by bundle!
		ControllerStateManagement.registerControllerCommand(new LightCommand());
		ControllerStateManagement.registerControllerDimensionCommand(new GatewayCommand());
		
		server.start();
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		server.stop();
	}
	
	

}
