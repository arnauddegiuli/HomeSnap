package com.homesnap.engine.controller.what;

import com.homesnap.engine.controller.Controller;

/**
 * This is the base class for implementing {@link StateValue} types which are defined into the states definition files (*.states) of controllers.
 * 
 * @see Controller
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public abstract class StateValueType implements StateValue {
	
	/**
	 * Updates the value which is defined by this type.
	 * @param value The value to update
	 * @throws IllegalArgumentException if the value is not compatible with this type
	 */
	public abstract void setValue(String value);
}
