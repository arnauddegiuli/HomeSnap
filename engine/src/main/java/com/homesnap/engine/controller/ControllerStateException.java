package com.homesnap.engine.controller;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ControllerStateException extends RuntimeException {
	
	/** */
	private static final long serialVersionUID = -5544916336424513208L;

	/**
	 * 
	 * @param msg
	 */
	public ControllerStateException(String msg) {
		super(msg);
	}
	
	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ControllerStateException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
