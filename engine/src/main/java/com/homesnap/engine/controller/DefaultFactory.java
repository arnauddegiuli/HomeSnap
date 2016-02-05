package com.homesnap.engine.controller;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.Monitor;

/**
 * 
 */
public abstract class DefaultFactory implements ControllerFactory {

	@Override
	public <C extends Controller> C createController(Class<C> clazz, String address) {
		
		C controller = null;
		try {
			controller = (C) clazz.getConstructor(String.class).newInstance(address);
			Monitor monitor = getMonitor();
			if (monitor != null) {
				monitor.addControllerToMonitor(controller);
			}
			Commander commander = getCommander();
			if (controller != null)  {
				commander.addControllerToExecute(controller);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controller;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract Monitor getMonitor();
	
	/**
	 * 
	 * @return
	 */
	protected abstract Commander getCommander();
}
