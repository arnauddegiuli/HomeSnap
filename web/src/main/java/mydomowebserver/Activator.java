package mydomowebserver;

/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private ServiceTracker simpleLogServiceTracker;
	private SimpleLogService simpleLogService;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		System.setProperty("jetty.home.bundle", "MyDomoWebServer");

		
		
		// register the service
		context.registerService(
				SimpleLogService.class.getName(), 
				new SimpleLogServiceImpl(), 
				new Hashtable());
		
		// create a tracker and track the log service
		simpleLogServiceTracker = 
			new ServiceTracker(context, SimpleLogService.class.getName(), null);
		simpleLogServiceTracker.open();
		
		// grab the service
		simpleLogService = (SimpleLogService) simpleLogServiceTracker.getService();

		if(simpleLogService != null)
			simpleLogService.log("Yee ha, I'm logging!");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if(simpleLogService != null)
			simpleLogService.log("Yee ha, I'm logging!");
		
		// close the service tracker
		simpleLogServiceTracker.close();
		simpleLogServiceTracker = null;
		
		simpleLogService = null;
	}

}
