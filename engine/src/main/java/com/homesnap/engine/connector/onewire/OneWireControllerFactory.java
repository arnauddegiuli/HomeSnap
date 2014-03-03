package com.homesnap.engine.connector.onewire;

import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.controller.DefaultFactory;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class OneWireControllerFactory extends DefaultFactory {

	/** */
	private static final OneWireControllerFactory factory = new OneWireControllerFactory();
	
	/** */
	private OneWireAdapter adapter;
	
	/**
	 * 
	 */
	private OneWireControllerFactory() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static OneWireControllerFactory getFactory() {
		return factory;
	}

	@Override
	protected Monitor getMonitor() {
		return getAdapter();
	}

	@Override
	protected Commander getCommander() {
		return getAdapter();
	}
	
	/**
	 * 
	 * @return
	 */
	private OneWireAdapter getAdapter() {
		if (adapter == null) {
			adapter = new OneWireAdapter();
		}
		return adapter;
	}
}
