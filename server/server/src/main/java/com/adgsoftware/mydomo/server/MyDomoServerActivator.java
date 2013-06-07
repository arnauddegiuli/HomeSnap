package com.adgsoftware.mydomo.server;

/*
 * #%L
 * MyDomoServer
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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MyDomoServerActivator implements BundleActivator {

	private Server server;
	
	@Override
	public void start(BundleContext arg0) throws Exception {
		server = new Server();
		
//		// TODO remove this thing... later will be loaded by bundle!
//		ControllerStateManagement.registerControllerCommand(new LightCommand());
//		ControllerStateManagement.registerControllerDimensionCommand(new GatewayCommand());
		
		server.start();
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		server.stop();
	}
	
	

}
