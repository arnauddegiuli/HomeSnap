package com.homesnap.engine.controller.properties;

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
