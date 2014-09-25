package com.homesnap.engine.properties;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class PropertyLoadException extends RuntimeException {

	/** */
	private static final long serialVersionUID = -5911184766690148642L;
	
	/** The line number in the configuration file which cause the exception */
	private int lineNumer;

	/**
	 * 
	 * @param message
	 */
	public PropertyLoadException(int lineNumer, String message) {
		super(message);
		this.lineNumer = lineNumer;
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public PropertyLoadException(int lineNumer, String message, Throwable cause) {
		super(message, cause);
		this.lineNumer = lineNumer;
	}

	@Override
	public String getMessage() {
		return new StringBuilder("Error line ").append(lineNumer).append(": ").append(super.getMessage()).toString();
	}
}
