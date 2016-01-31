package com.homesnap.engine.controller.properties;

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
