package com.homesnap.engine.configuration;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
