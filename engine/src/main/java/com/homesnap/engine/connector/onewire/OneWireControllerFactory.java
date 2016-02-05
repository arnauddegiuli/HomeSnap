//package com.homesnap.engine.connector.onewire;
//
///*
// * #%L
// * HomeSnapEngine
// * %%
// * Copyright (C) 2011 - 2015 A. de Giuli
// * %%
// * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
// *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
// * 
// *     HomeSnap is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     (at your option) any later version.
// * 
// *     HomeSnap is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU General Public License for more details.
// * 
// *     You should have received a copy of the GNU General Public License
// *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
// * #L%
// */
//
//
//import com.homesnap.engine.connector.Commander;
//import com.homesnap.engine.connector.Monitor;
//import com.homesnap.engine.controller.DefaultFactory;
//
///**
// * 
// * @author DRIESBACH Olivier
// * @version 1.0
// * @since 1.0
// */
//public class OneWireControllerFactory extends DefaultFactory {
//
//	/** */
//	private static final OneWireControllerFactory factory = new OneWireControllerFactory();
//	
//	/** */
//	private OneWireAdapter adapter;
//	
//	/**
//	 * 
//	 */
//	private OneWireControllerFactory() {
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public static OneWireControllerFactory getFactory() {
//		return factory;
//	}
//
//	@Override
//	protected Monitor getMonitor() {
//		return getAdapter();
//	}
//
//	@Override
//	protected Commander getCommander() {
//		return getAdapter();
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	private OneWireAdapter getAdapter() {
//		if (adapter == null) {
//			adapter = new OneWireAdapter();
//		}
//		return adapter;
//	}
//}
