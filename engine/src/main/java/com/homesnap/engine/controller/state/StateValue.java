package com.homesnap.engine.controller.state;

import com.homesnap.engine.controller.Commander;

/**
 * Represents the value of a state.
 * 
 * @see Commander#getStatesToString()
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public interface StateValue {

	/**
	 * Return the value of the state.
	 * @return The string representation of the value
	 */
	public String getValue();
}
