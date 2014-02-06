package com.adgsoftware.mydomo.engine.controller;

/**
 * 
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
