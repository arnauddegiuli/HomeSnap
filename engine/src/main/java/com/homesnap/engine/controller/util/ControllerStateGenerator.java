package com.homesnap.engine.controller.util;

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


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.properties.StateSection;
import com.homesnap.engine.controller.properties.StatesReader;
import com.homesnap.engine.controller.types.ListOfValuesType;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.StateValueType;

/**
 * Utility class to generate {@link StateValue} enum classes for user-defined controllers.
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ControllerStateGenerator extends Generator {
	
	/** Indicates if the java enumeration state files must be overwritten */
	private boolean forceUpdate;
	
	private static final String STATE_NAME_TEMPLATE = "stateNameEnumTemplate";
	private static final String LIST_OF_VALUES_TEMPLATE = "listOfValuesEnumTemplate";
	
	/**
	 * 
	 */
	public ControllerStateGenerator() {
		super();
	}
	
	@Override
	protected String getResourceExtension() {
		return Controller.STATES_EXTENSION;
	}

	@Override
	public void generateFiles(String outputPath) {
		generateFiles(outputPath, false);
	}
	
	/**
	 * 
	 * @param outputPath
	 * @param forceUpdate
	 */
	private void generateFiles(String outputPath, boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
		super.generateFiles(outputPath);
	}
	
	/**
	 * 
	 * @param resource
	 */
	protected void generateSourceFiles(File resource) {
		
		String packagePath = resource.getParentFile().getAbsolutePath().substring(getClasspath().length());
		String controllerClassName = resource.getName().substring(0, (resource.getName().length() - Controller.STATES_EXTENSION.length()));
		String stateNameClass = controllerClassName.concat(StateName.class.getSimpleName());
		Map<String, String> tokens = new HashMap<String, String>();
		
		// Determines if the states definition file (.states) located in the source path is more recent than the java source file which defines the state names of the controller.
		File javaSourceFile = new File(createFilePath(stateNameClass +".java", getOutputPath(), packagePath));
		if (!forceUpdate && javaSourceFile.exists() && javaSourceFile.lastModified() > resource.lastModified()) {
			return;
		}
		// Load the definition file
		StatesReader reader = new StatesReader();
		try {
			reader.load(resource);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load states definition file "+ resource.getAbsolutePath(), e);
		}
		// Checks if the "generate_once" property is set to "true" to not overwrite java enum class if exists
		if ("true".equals(reader.getSection(StatesReader.ENGINE_SECTION).getProperty(StatesReader.GENERATE_ONCE).getValue()) && javaSourceFile.exists()) {
			return;
		}
		// Orders the state names by name
		StateSection controllerSection = reader.getControllerSection();
		String[] stateNames = controllerSection.getStateNames().toArray(new String[]{});
		Arrays.sort(stateNames);
		StringBuilder enumValues = new StringBuilder();
		String separator = ","+ EOL;
		
		tokens.put("@PACKAGE_NAME@", packagePath.replace(File.separatorChar, '.'));
		tokens.put("@STATE_VALUE_CLASS@", StateValue.class.getName());
		tokens.put("@GENERATOR_NAME@", getClass().getSimpleName());
		tokens.put("@STATE_VALUE_CLASSNAME@", StateValue.class.getSimpleName());
		
		for (String stateName : stateNames) {
			enumValues.append("\t").append(generateJavadoc(reader, stateName)).append(EOL)
				.append("\t").append(stateName.toUpperCase()).append(separator);
			
			// If the state value's type is a list of values, generates the java enumeration class which defines all possible values of the state name.
			StateValueType type = controllerSection.getProperty(stateName).getType();
			if (type instanceof ListOfValuesType) {
				
				String stateValueClass = new StringBuilder(controllerClassName)
					.append(stateName.substring(0, 1).toUpperCase()).append(stateName.substring(1))
					.append(StateValue.class.getSimpleName()).toString();
				
				tokens.put("@STATE_NAME_CONSTANT@", generateStateNameLink(stateNameClass, stateName));
				tokens.put("@ENUMERATION_NAME@", stateValueClass);
				tokens.put("@STATE_VALUE_VALUES@", splitValues(((ListOfValuesType) type).getValues(), separator));
				generateEnumerationClass(new File(createFilePath(stateValueClass +".java", getOutputPath(), packagePath)), LIST_OF_VALUES_TEMPLATE, tokens);
			}
		}
		enumValues.setLength(enumValues.length() - separator.length());
		enumValues = enumValues.append(";");
		
		// Generates the java enumeration class file which defines all state names of the controller.
		tokens.clear();
		tokens.put("@PACKAGE_NAME@", packagePath.replace(File.separatorChar, '.'));
		tokens.put("@STATE_NAME_CLASS@", StateName.class.getName());
		tokens.put("@CONTROLER_NAME@", controllerClassName);
		tokens.put("@GENERATOR_NAME@", getClass().getSimpleName());
		tokens.put("@ENUMERATION_NAME@", stateNameClass);
		tokens.put("@STATE_NAME_CLASSNAME@", StateName.class.getSimpleName());
		tokens.put("@STATE_NAME_VALUES@", enumValues.toString());
		generateEnumerationClass(javaSourceFile, STATE_NAME_TEMPLATE, tokens);
	}
	
	/**
	 * 
	 * @param stateDefinition
	 * @param stateName
	 * @return
	 */
	private String generateJavadoc(StatesReader stateDefinition, String stateName) {
		
		String javadoc = stateDefinition.getPropertyValue(StatesReader.DOCUMENTATION_SECTION, stateName);
		StringBuilder result = new StringBuilder("/** ");
		if (javadoc == null) {
			javadoc = "State name "+ stateName;
		}
		return result.append(javadoc).append(" */").toString();
	}
	
	/**
	 * 
	 * @param stateNameClass
	 * @param stateName
	 * @return
	 */
	private String generateStateNameLink(String stateNameClass, String stateName) {
		return "{@link "+ stateNameClass +"#"+ stateName.toUpperCase() +"}";
	}
	
	/**
	 * 
	 * @param output
	 * @param templateName
	 * @param tokens
	 */
	private void generateEnumerationClass(File output, String templateName, Map<String, String> tokens) {
		String javaContent = getTemplateContent(templateName);
		for (Entry<String, String> entry : tokens.entrySet()) {
			javaContent = javaContent.replace(entry.getKey(), entry.getValue());
		}
		writeFile(output, javaContent);
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private String splitValues(String[] values, String separator) {
		StringBuilder result = new StringBuilder();
		for (String value : values) {
			result.append("\t").append(value.toUpperCase()).append(separator);
		}
		result.setLength(result.length() - separator.length());
		return result.append(";").toString();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String srcDir = null;
		if (args.length == 1) {
			srcDir = args[0];
		}
		new ControllerStateGenerator().generateFiles(srcDir);
	}
}
