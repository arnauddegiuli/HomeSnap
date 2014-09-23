package com.homesnap.engine.controller.what;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StateDefinition {
	
	public static final String ENGINE_SECTION = "Engine";
	public static final String CONTROLLER_SECTION = "Controller";
	public static final String DOCUMENTATION_SECTION = "Documentation";
	
	private static final String GENERATE_ONCE = "generate_once";
	private static final String VERSION = "version";
	
	private Map<String, Map<String, String>> sections;
	
	private int lineNumber;
	
	/**
	 * 
	 */
	public StateDefinition() {
		lineNumber = 0;
		sections = new HashMap<String, Map<String,String>>();
	}
	
	/**
	 * 
	 * @param sectionName
	 * @param propertyName
	 * @return
	 */
	public String getSectionProperty(String sectionName, String propertyName) {
		return getSectionProperties(sectionName).get(propertyName);
	}
	
	/**
	 * 
	 * @param sectionName
	 * @return
	 */
	public Map<String, String> getSectionProperties(String sectionName) {
		Map<String, String> result = sections.get(sectionName);
		if (result == null) {
			result = Collections.emptyMap();
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean mustBeGeneratedOnce() {
		return "true".equalsIgnoreCase(getSectionProperty(ENGINE_SECTION, GENERATE_ONCE));
	}
	
	/**
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void load(String filename) throws FileNotFoundException, IOException {
		load(new File(filename));
	}
	
	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void load(File file) throws FileNotFoundException, IOException {
		load(new FileInputStream(file));
	}
	
	/**
	 * 
	 * @param input
	 * @throws IOException 
	 */
	public void load(InputStream input) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input));
			String line = readNextProperty(reader); // Verifies that the first property is a section
			if (isValidSectionName(line)) {
				loadSection(reader, line);
			} else {
				throw new StateDefinitionException(lineNumber, "States definition file must start with a section name [<section name>].");
			}
			while ((line = readNextProperty(reader)) != null) {
				if (isValidSectionName(line)) {
					loadSection(reader, line);
				}
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @param sectionName
	 * @return
	 */
	private boolean isValidSectionName(String sectionName) {
		return (sectionName.charAt(0) == '[' && sectionName.length() > 2 && sectionName.charAt(sectionName.length()-1) == ']');
	}
	
	/**
	 * 
	 * @param reader
	 * @param sectionLine
	 * @throws IOException 
	 */
	private void loadSection(BufferedReader reader, String sectionLine) throws IOException {
		
		String sectionName = sectionLine.substring(1, sectionLine.length() - 1);
		if (ENGINE_SECTION.equalsIgnoreCase(sectionName)) {
			sectionName = ENGINE_SECTION;
		} else if (CONTROLLER_SECTION.equalsIgnoreCase(sectionName)) {
			sectionName = CONTROLLER_SECTION;
		} else if (DOCUMENTATION_SECTION.equalsIgnoreCase(sectionName)) {
			sectionName = DOCUMENTATION_SECTION;
		}
		
		Map<String, String> sectionValues;
		if (isSectionEmpty(sectionName)) {
			sectionValues = new HashMap<String, String>();
			sections.put(sectionName, sectionValues);
		} else {
			throw new StateDefinitionException(lineNumber, "Invalid section name "+ sectionName);
		}
		
		String line = null;
		while ((line = readNextProperty(reader)) != null) {
			if (line.charAt(0) == '[') { // New section
				loadSection(reader, line);
				break;
			}
			int separatorPos = line.indexOf("=");
			if (separatorPos == -1) {
				throw new StateDefinitionException(lineNumber, "Invalid property, missing '=' character.");
			}
			sectionValues.put(line.substring(0, separatorPos), line.substring(separatorPos + 1));
		}
	}
	
	private boolean isSectionEmpty(String sectionName) {
		return !sections.containsKey(sectionName);
	}
	
	/**
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private String readNextProperty(BufferedReader reader) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			if (line.isEmpty() || (line = line.trim()).isEmpty()) {
				continue;
			}
			break;
		}
		return line;
	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void save(String output) throws IOException {
		
		File outputFile = new File(output);
		FileWriter writer = null;
		if (!outputFile.exists()) {
			try {
				outputFile.mkdirs();
				writer = new FileWriter(outputFile);
				writeSection(writer, ENGINE_SECTION);
				writeSection(writer, CONTROLLER_SECTION);
				writeSection(writer, DOCUMENTATION_SECTION);
				writer.flush();
			} catch (Exception e) {
				throw new IOException("Unable to save definition file to "+ output, e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param writer
	 * @param name
	 * @throws IOException 
	 */
	private void writeSection(Writer writer, String sectionName) throws IOException {
		Map<String, String> properties = getSectionProperties(sectionName);
		if (!properties.isEmpty()) {
			writer.write("["+ sectionName +"]");
			for (Entry<String, String> entry : properties.entrySet()) {
				writer.write(entry.getKey() +"="+ entry.getValue());
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new StateDefinition().load("C:/Projets/HomeSnap/engine/src/main/java/com/homesnap/engine/controller/light/Light.states2");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
