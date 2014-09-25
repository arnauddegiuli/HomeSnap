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
	 * Indicates if a state value can be set into this type.
	 * @param stateValue The state value to test
	 * @return <code>true</code> if the value is compatible with this type and <code>false</code> otherwise.
	 */
	public void setValue(StateValue stateValue) {
		if (stateValue == null) {
			throw new NullPointerException("Could not set null value.");
		}
		setValue(stateValue.getValue());
	}
	
	/**
	 * Indicates if a state value can be set into this type.
	 * @param value The value to test
	 * @return <code>true</code> if the value is compatible with this type and <code>false</code> otherwise.
	 */
	protected abstract void setValue(String value);
}
