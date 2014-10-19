package com.homesnap.engine.configuration;

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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the base class which represents the loading mecanism of the HomeSnap engine for configuration files. 
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public abstract class ConfigurationReader {

	/** Contains the current line number after each {@link #readNextLine(BufferedReader)} method call */
	private int lineNumber;
	
	/** List of section names with their properties (key=value) */
	private Map<String, Section<? extends Property>> sections = new HashMap<String, Section<? extends Property>>();
	
	/**
	 * 
	 */
	protected ConfigurationReader() {
		lineNumber = 0;
	}
	
	/**
	 * Returns the current line number when reading configuration file
	 * @return the current line number when reading configuration file
	 */
	protected int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Returns a section from its name
	 * @param name The section name
	 * @return the complete section definition
	 */
	public Section<? extends Property> getSection(String name) {
		return sections.get(name);
	}
	
	/**
	 * Returns the list of sections found in the configuration file
	 * @return the list of sections found in the configuration file if it has been loaded and an empty map otherwise
	 */
	public Collection<Section<? extends Property>> getSections() {
		return Collections.unmodifiableCollection(sections.values());
	}
	
	/**
	 * Returns the property object of a section
	 * @param sectionName The section name
	 * @param propertyName The property name
	 * @return the property object if it has been found and null otherwise
	 */
	public Property getProperty(String sectionName, String propertyName) {
		Property result = null;
		Section<? extends Property> section = getSection(sectionName);
		if (section != null) {
			result = section.getProperty(propertyName);
		}
		return result;
	}
	
	/**
	 * Returns the value of a property name of a section
	 * @param sectionName The section name
	 * @param propertyName The property name
	 * @return the value of the property if it has been found and null otherwise
	 */
	public String getPropertyValue(String sectionName, String propertyName) {
		String result = null;
		Property property = getProperty(sectionName, propertyName);
		if (property != null) {
			result = property.getValue();
		}
		return result;
	}
	
	/**
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public synchronized void load(String filename) throws FileNotFoundException, IOException {
		loadStream(new FileInputStream(new File(filename)));
	}
	
	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public synchronized void load(File file) throws FileNotFoundException, IOException {
		loadStream(new FileInputStream(file));
	}
	
	/**
	 * 
	 * @param input
	 * @throws IOException 
	 */
	public synchronized void load(InputStream input) throws IOException {
		loadStream(input);
	}
	
	/**
	 * 
	 * @param input
	 * @throws IOException
	 */
	private void loadStream(InputStream input) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input));
			String line = readNextLine(reader);
			String sectionName = null;
			
			// Check that the first non-empty line is a valid section name
			if (isSectionDeclaration(line)) {
				sectionName = extractSectionName(line);
			}
			else {
				throw new ConfigurationException(lineNumber, "Configuration must starts with a section name [<section name>].");
			}
			Section<? extends Property> section = createSection(sectionName);
			addSection(section);
			
			// Load the stream entirely
			while ((line = readNextLine(reader)) != null) {
				if (isSectionDeclaration(line)) {
					sectionName = extractSectionName(line);
					if (isValidSectionName(sectionName)) { // Check that the section has not been already defined (duplicated section in the same file)
						if (sectionExists(sectionName)) {
							throw new ConfigurationException(lineNumber, "Duplicated section name "+ sectionName);
						}
						section = createSection(sectionName);
						addSection(section);
						continue; // Read next line
					}
					throw new ConfigurationException(lineNumber, "Invalid section name "+ sectionName);
				}
				// Extract property name and value
				String separator = "=";
				int pos = line.indexOf(separator);
				if (pos == -1) {
					throw new ConfigurationException(lineNumber, "Invalid property definition, missing '=' character.");
				}
				
				String property = line.substring(0, pos);
				String value = "";
				if (pos < line.length()) {
					pos += separator.length();
				}
				value = line.substring(pos);
				onProperty(section.getName(), property, value);
				section.addProperty(property, value);
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
	 * Adds a section into the current list
	 * @param section The new section read
	 */
	private void addSection(Section<? extends Property> section) {
		sections.put(section.getName(), section);
	}
	
	/**
	 * Indicates if the line read in the configuration file match with a section declaration
	 * @param line the line read in the configuration file
	 * @return <code>true</code> if the line is delimited by the [] characters and <code>false</code> otherwise 
	 */
	private boolean isSectionDeclaration(String line) {
		return (line.charAt(0) == '[' && line.charAt(line.length()-1) == ']');
	}
	
	/**
	 * 
	 * @param line
	 * @return
	 */
	private String extractSectionName(String line) {
		return line.substring(1, line.length()-1);
	}
	
	/**
	 * Indicates if a section name is valid in this configuration
	 * @param name The name of the section to test
	 * @return
	 */
	protected boolean isValidSectionName(String name) {
		return getSectionNames().contains(name);
	}
	
	/**
	 * Indicates if a section is present in this configuration
	 * @param name The name of the section to test
	 * @return
	 */
	private boolean sectionExists(String name) {
		return sections.containsKey(name);
	}
	
	/**
	 * Read the next non empty line of the configuration file
	 * @param reader The reader of the configuration file
	 * @return
	 * @throws IOException
	 */
	private String readNextLine(BufferedReader reader) throws IOException {
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
				for (Section<? extends Property> section : sections.values()) {
					writeSection(writer, section);
				}
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
	 * @param section
	 * @throws IOException 
	 */
	private void writeSection(Writer writer, Section<? extends Property> section) throws IOException {
		if (!section.isEmpty()) {
			writer.write("["+ section.getName() +"]");
			for (Property property : section.getProperties()) {
				writer.write(property.getName() +"="+ property.getValue());
			}
		}
	}
	
	/**
	 * Returns all section names that are valid into this configuration reader
	 * @return
	 */
	protected abstract Collection<String> getSectionNames();
	
	/**
	 * This method is called during the loading process of a configuration file.
	 * Each time a new section name has been discovered into the configuration file the method must create
	 * the implementation object of this section. 
	 * @param name The section name
	 * @return
	 */
	protected abstract Section<? extends Property> createSection(String name);
	
	/**
	 * 
	 * @param sectionName
	 * @param property
	 * @param value
	 * @return
	 */
	protected void onProperty(String sectionName, String property, String value) {
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	protected Section<Property> createDefaultSection(String name) {
		return new Section<Property>(name) {

			@Override
			protected Property createProperty(final String name, final String value) {
				return new Property() {
					
					@Override
					public String getName() {
						return name;
					}
					
					@Override
					public String getValue() {
						return value;
					}
				};
			}
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public static ConfigurationReader getDefaultReader() {
		return new ConfigurationReader() {
			
			@Override
			protected Collection<String> getSectionNames() {
				return null;
			}
			
			@Override
			protected boolean isValidSectionName(String name) {
				return true;
			}
			
			@Override
			protected Section<? extends Property> createSection(String name) {
				return createDefaultSection(name);
			}
		};
	}
}
