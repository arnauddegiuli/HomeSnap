package com.homesnap.engine.controller.what;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import com.homesnap.engine.properties.ConfigurationProperties;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StateProperties extends ConfigurationProperties {
	
	public static final String ENGINE_SECTION = "Engine";
	public static final String CONTROLLER_SECTION = "Controller";
	public static final String DOCUMENTATION_SECTION = "Documentation";
	
	private static final String GENERATE_ONCE = "generate_once";
	private static final String VERSION = "version";
	
	/**
	 * 
	 */
	public StateProperties() {
		super();
	}
	
	@Override
	protected Collection<String> getSectionNames() {
		return Arrays.asList(ENGINE_SECTION, CONTROLLER_SECTION, DOCUMENTATION_SECTION);
	}

	/**
	 * 
	 * @return
	 */
	public boolean mustBeGeneratedOnce() {
		return "true".equalsIgnoreCase(getSectionProperty(ENGINE_SECTION, GENERATE_ONCE));
	}
	
	public static void main(String[] args) {
		try {
			new StateProperties().load("C:/Projets/HomeSnap/engine/src/main/java/com/homesnap/engine/controller/light/Light.states");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
