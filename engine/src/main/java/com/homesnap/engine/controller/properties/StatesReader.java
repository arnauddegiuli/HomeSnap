package com.homesnap.engine.controller.properties;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import com.homesnap.engine.configuration.ConfigurationReader;
import com.homesnap.engine.configuration.Property;
import com.homesnap.engine.configuration.Section;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StatesReader extends ConfigurationReader {
	
	public static final String ENGINE_SECTION = "Engine";
	public static final String CONTROLLER_SECTION = "Controller";
	public static final String DOCUMENTATION_SECTION = "Documentation";
	
	public static final String GENERATE_ONCE = "generate_once";
	public static final String VERSION = "version";
	
	/**
	 * 
	 */
	public StatesReader() {
		super();
	}
	
	@Override
	protected Collection<String> getSectionNames() {
		return Arrays.asList(ENGINE_SECTION, CONTROLLER_SECTION, DOCUMENTATION_SECTION);
	}
	
	@Override
	protected Section<? extends Property> createSection(String name) {
		if (CONTROLLER_SECTION.equalsIgnoreCase(name)) {
			return new StateSection(name);
		}
		return createDefaultSection(name);
	}
	
	public StateSection getControllerSection() {
		return (StateSection) getSection(CONTROLLER_SECTION);
	}
	
	public static void main(String[] args) {
		try {
			new StatesReader().load("C:/Projets/HomeSnap/engine/src/main/java/com/homesnap/engine/controller/light/Light.states");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
