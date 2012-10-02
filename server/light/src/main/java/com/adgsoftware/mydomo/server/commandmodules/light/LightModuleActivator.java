package com.adgsoftware.mydomo.server.commandmodules.light;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class LightModuleActivator implements BundleActivator {

	private LightCommand lightModule = new LightCommand();
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		ControllerStateManagement.registerControllerCommand(lightModule);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		ControllerStateManagement.unRegisterControllerCommand(lightModule);
	}
	
	

}
