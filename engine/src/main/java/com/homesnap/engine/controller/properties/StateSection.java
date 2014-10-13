package com.homesnap.engine.controller.properties;

import java.util.Collections;
import java.util.Set;

import com.homesnap.engine.configuration.Section;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StateSection extends Section<StateProperty> {
	
	/**
	 * 
	 * @param name
	 */
	StateSection(String name) {
		super(name);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getStateNames() {
		return Collections.unmodifiableSet(getPropertiesMap().keySet());
	}

	@Override
	protected StateProperty createProperty(String name, String value) {
		return new StateProperty(name, value);
	}
}
