package com.homesnap.engine.controller.what;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StateDefinitionException extends RuntimeException {

	/** */
	private static final long serialVersionUID = -5911184766690148642L;
	
	/** */
	private int line;

	/**
	 * 
	 * @param message
	 */
	public StateDefinitionException(int line, String message) {
		super(message);
		this.line = line;
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public StateDefinitionException(int line, String message, Throwable cause) {
		super(message, cause);
		this.line = line;
	}

	@Override
	public String getMessage() {
		return new StringBuilder("Error line ").append(line).append(": ").append(super.getMessage()).toString();
	}
}
