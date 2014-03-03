package com.homesnap.engine.connector.knxnetip;

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
