package com.homesnap.engine.connector.knxnetip;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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
import com.homesnap.engine.controller.DefaultFactory;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXControllerFactory extends DefaultFactory {
	
	/** */
	private static final KNXControllerFactory factory = new KNXControllerFactory();

	/**
	 * 
	 */
	private KNXControllerFactory() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static KNXControllerFactory getFactory() {
		return factory;
	}

	/** */
	private Monitor monitor;
	
	/** */
	private Commander commander;

	@Override
	protected Monitor getMonitor() {
		if (monitor == null) {
			monitor = new KNXMonitor("192.168.92.1");
		}
		return monitor;
	}

	@Override
	protected Commander getCommander() {
		if (commander == null) {
			commander = new KNXCommander("192.168.92.1");
		}
		return commander;
	}
}
