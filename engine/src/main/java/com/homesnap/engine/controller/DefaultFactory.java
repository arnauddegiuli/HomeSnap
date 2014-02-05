package com.homesnap.engine.controller;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
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
			Commander commander = getController();
			if (controller != null)  {
				commander.addControllerToControl(controller);
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
	protected abstract Commander getController();
}
