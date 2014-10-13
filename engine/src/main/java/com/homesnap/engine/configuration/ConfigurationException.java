package com.homesnap.engine.configuration;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationException extends RuntimeException {

	/** */
	private static final long serialVersionUID = -5911184766690148642L;
	
	/** The line number in the configuration file which cause the exception */
	private int lineNumer;

	/**
	 * 
	 * @param message
	 */
	public ConfigurationException(int lineNumer, String message) {
		super(message);
		this.lineNumer = lineNumer;
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(int lineNumer, String message, Throwable cause) {
		super(message, cause);
		this.lineNumer = lineNumer;
	}

	@Override
	public String getMessage() {
		return new StringBuilder("Error line ").append(lineNumer).append(": ").append(super.getMessage()).toString();
	}
}
