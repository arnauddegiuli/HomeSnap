package com.homesnap.engine.controller;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public interface ControllerFactory {
	
	/**
	 * 
	 * @param clazz
	 * @param address
	 * @return
	 */
	public <C extends Controller> C createController(Class<C> clazz, String address);
}
