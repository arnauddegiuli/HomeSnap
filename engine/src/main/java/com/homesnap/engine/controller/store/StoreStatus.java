package com.homesnap.engine.controller.store;

import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum StoreStatus implements StateValue {
	
	/**
	 * Indicates that the store is entirely open
	 */
	UP("up"),
	
	/**
	 * Indicates that the store is entirely closed
	 */
	DOWN("down"),
	
	/**
	 * Indicates that the store is opening
	 */
	OPENING("opening"),
	
	/**
	 * Indicates that the store is closing
	 */
	CLOSING("closing");

	/** The value of the status */
	private String value;
	
	private StoreStatus(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
}
