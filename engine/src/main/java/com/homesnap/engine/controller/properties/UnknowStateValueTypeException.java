package com.homesnap.engine.controller.properties;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class UnknowStateValueTypeException extends RuntimeException {

	/** */
	private static final long serialVersionUID = -443695062796116205L;
	
	/**
	 * 
	 * @param msg
	 */
	public UnknowStateValueTypeException(String msg) {
		super(msg);
	}
}
