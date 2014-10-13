package com.homesnap.engine.configuration;

/**
 * Represents a simple key/value pair
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public interface Property {
	
	/**
	 * Returns the name of the key
	 * @return the name of the key
	 */
	public String getName();
	
	/**
	 * Returns the value associated to the name
	 * @return the value associated to the name
	 */
	public String getValue();
}
