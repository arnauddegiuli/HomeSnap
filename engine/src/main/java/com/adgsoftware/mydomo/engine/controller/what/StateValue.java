package com.adgsoftware.mydomo.engine.controller.what;


/**
 * A tpye of Value. This interface can be implemented by two ways:
 * <ul>
 * <li>An enumeration of limited values. Example: ON/OFF or STOP/UP/DOWN, etc..</li>
 * <li>A classe reprensenting a large or infinites possible values Example: a temperature, a time, etc..</li>
 * </ul>
 * @see States
 * 
 * @version 1.0
 * @since 1.0
 */
public interface StateValue {

	/**
	 * Return a string representation of the value from this type.
	 * @return a value.
	 */
	public String getValue();
}
