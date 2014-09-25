package com.homesnap.engine.properties;

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
import java.util.Map.Entry;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public abstract class ConfigurationProperties {

	/** List of section names with their properties (key=value) */
	private Map<String, Map<String, String>> sections = new HashMap<String, Map<String,String>>();
	
	/** The current line number when load method is called */
	private int lineNumber;
	
	/**
	 * 
	 */
	public ConfigurationProperties() {
		lineNumber = 0;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract Collection<String> getSectionNames();
	
	/**
	 * Returns the value associated to a property of a section.
	 * @param sectionName The section name where the property is defined
	 * @param propertyName The name of the property
	 * @return the value associated to the property
	 */
	public String getSectionProperty(String sectionName, String propertyName) {
		return getSectionProperties(sectionName).get(propertyName);
	}
	
	/**
	 * Returns all properties with their associated value of a section.
	 * @param sectionName The section name
	 * @return all properties with their associated value
	 */
	public Map<String, String> getSectionProperties(String sectionName) {
		Map<String, String> result = null;
		synchronized (sections) {
			result = sections.get(sectionName);
			if (result == null) {
				result = Collections.emptyMap();
			}
		}
		return Collections.unmodifiableMap(result);
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
			} else {
				throw new PropertyLoadException(lineNumber, "Configuration must starts with a section name [<section name>].");
			}
			
			// Load the stream entirely
			while ((line = readNextLine(reader)) != null) {
				if (isSectionDeclaration(line)) {
					sectionName = extractSectionName(line);
					// Check that the section name read is not already defined (duplicated section in the same file)
					if (getSectionNames().contains(sectionName)) {
						if (isSectionEmpty(sectionName)) {
							continue;
						} else {
							throw new PropertyLoadException(lineNumber, "Duplicated section name "+ sectionName);
						}
					} else {
						throw new PropertyLoadException(lineNumber, "Invalid section name "+ sectionName);
					}
				}
				// Extract property name and value
				int separator = line.indexOf("=");
				if (separator == -1) {
					throw new PropertyLoadException(lineNumber, "Invalid property definition, missing '=' character.");
				}
				
				String property = line.substring(0, separator);
				String value = "";
				if (separator < (line.length()-1)) {
					value = line.substring(separator+1);
				}
				synchronized (sections) {
					Map<String, String> result = sections.get(sectionName);
					if (result == null) {
						result = new HashMap<String, String>();
						sections.put(sectionName, result);
					}
					result.put(property, value);
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
	 * @param value
	 * @return
	 */
	private boolean isSectionDeclaration(String value) {
		return (value.charAt(0) == '[' && value.charAt(value.length()-1) == ']');
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
	 * 
	 * @param sectionName
	 * @return
	 */
	private boolean isSectionEmpty(String sectionName) {
		return !sections.containsKey(sectionName);
	}
	
	/**
	 * 
	 * @param reader
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
				for (String sectionName : getSectionNames()) {
					writeSection(writer, sectionName);
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
}
