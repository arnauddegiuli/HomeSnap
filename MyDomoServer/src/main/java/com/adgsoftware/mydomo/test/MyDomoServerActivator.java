package com.adgsoftware.mydomo.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MyDomoServerActivator implements BundleActivator {

	private Server server;
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		server = new Server();
		server.start();
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		server.stop();
	}
	
	

}
