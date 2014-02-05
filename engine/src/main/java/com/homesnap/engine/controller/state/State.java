package com.homesnap.engine.controller.state;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public final class State {
	
	/** */
	private StateName state;
	
	/** */
	private StateValue value;

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public State(StateName state, StateValue value) {
		this.state = state;
		this.value = value;
	}

	/**
	 * 
	 * @return
	 */
	public StateName getName() {
		return state;
	}

	/**
	 * 
	 * @return
	 */
	public StateValue getValue() {
		return value;
	}
}
