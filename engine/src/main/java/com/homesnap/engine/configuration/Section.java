package com.homesnap.engine.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * In the HomeSnap engine, the configuration files which are loaded by the configuration readers contains some sections.
 * A section in a configuration file has unique identifier (its name) and contains some properties.
 * 
 * @see ConfigurationReader
 * @see Property
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public abstract class Section<P extends Property> {
	
	/** The name of the section */
	private String name;
	
	/** List of all properties defined in the section */
	private Map<String, P> properties = new HashMap<String, P>();
	
	/**
	 * 
	 * @param name
	 */
	protected Section(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public P getProperty(String name) {
		return properties.get(name);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	protected void addProperty(String name, String value) {
		if (properties.containsKey(name)) {
//			throw new DuplicatedPropertyException(name);
		}
		properties.put(name, createProperty(name, value));
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<P> getProperties() {
		return Collections.unmodifiableCollection(properties.values());
	}
	
	@Override
	public String toString() {
		return "Section ["+ name +"]";
	}

	/**
	 * Returns the properties map for child classes use
	 * @return the properties map
	 */
	protected Map<String, P> getPropertiesMap() {
		return Collections.unmodifiableMap(properties);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected abstract P createProperty(String name, String value);
}
