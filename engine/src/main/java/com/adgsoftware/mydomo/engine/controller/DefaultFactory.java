package com.adgsoftware.mydomo.engine.controller;

import com.adgsoftware.mydomo.engine.connector.Commander;
import com.adgsoftware.mydomo.engine.connector.Monitor;

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
