package com.adgsoftware.mydomo.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MyDomoServerActivator implements BundleActivator {

	@Override
	public void start(BundleContext arg0) throws Exception {
		Server.main(null);
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO refactor server for a better integration
		
	}

}
