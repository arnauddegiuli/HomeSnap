package com.homesnap.recorder;

/*
 * #%L
 * HomeSnapRecorder
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
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

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final Logger logger = Logger.getLogger(Activator.class
			.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {

//		System.out.print(context.);
//		System.setProperty("jetty.home.bundle", "HomeSnapWebServer");

		// register the service

		logger.info("STARTING RECORDER SERVICE BUNDLE");

//		httpServiceTracker = new ServiceTracker<HttpService, Object>(context,
//				HttpService.class.getName(), null) {
//			@Override
//			public Object addingService(
//					final ServiceReference<HttpService> serviceRef) {
//				httpService = (HttpService) super.addingService(serviceRef);
//				registerServlets();
//				registerResources();
//				return httpService;
//			}
//
//			@Override
//			public void removedService(final ServiceReference<HttpService> ref,
//					final Object service) {
//				if (httpService == service) {
//					unregisterServlets();
//					unregisterResources();
//					httpService = null;
//				}
//				super.removedService(ref, service);
//			}
//
//		};
//
//		httpServiceTracker.open();

		new RecorderServiceImpl("192.168.1.35", 20000, 12345);
		logger.info("RECORDER SERVICE BUNDLE STARTED");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {


	}



	
	
}
