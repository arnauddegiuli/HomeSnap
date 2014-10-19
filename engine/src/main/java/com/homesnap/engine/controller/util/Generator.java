package com.homesnap.engine.controller.util;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.homesnap.engine.controller.what.StateValue;

/**
 * Utility class to generate {@link StateValue} enum classes for user-defined controllers.
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public abstract class Generator {

	/** Location of the output path for generated java source files */
	private String outputPath;
	
	/** Location of the HomeSnap binaries */
	private String classPath;
	
	/** List of templates names with their content */
	private Map<String, String> templates = new HashMap<String, String>();
	
	/** End of line */
	protected static final String EOL = "\r\n";
	
	/**
	 * 
	 */
	protected Generator() {
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getClasspath() {
		return classPath;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getOutputPath() {
		return outputPath;
	}
	
	/**
	 * 
	 * @param outputPath
	 */
	public void generateFiles(String outputPath) {
		// Find the root resource folder
		URL basePath = getClass().getResource("/");
		if (basePath == null) {
			throw new RuntimeException("Unable to find resource folder.");
		}
		classPath = basePath.getFile();
		classPath = new File(classPath).getAbsolutePath();
		
		// Get the default output path if not specified and check if writeable 
		if (outputPath == null) {
			outputPath = new File(classPath.substring(0, (classPath.length() - "target/classes/".length())).concat("/src/main/java/")).getAbsolutePath();
		}
		File sourceFolder = new File(outputPath);
		if (!sourceFolder.canWrite()) {
			throw new RuntimeException("Could not generate output files, output folder "+ outputPath +" is not writeable.");
		}
		this.outputPath = sourceFolder.getAbsolutePath();
		generateRuntimeClasses(sourceFolder);
	}
	
	/**
	 * 
	 * @param sourceFolder
	 */
	private void generateRuntimeClasses(File sourceFolder) {
		for (File stateFile : sourceFolder.listFiles()) {
			if (stateFile.isDirectory()) {
				generateRuntimeClasses(stateFile);
			}
			else if (stateFile.getName().endsWith(getResourceExtension())) {
				generateSourceFiles(stateFile);
			}
        }
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract String getResourceExtension();
	
	/**
	 * 
	 * @param resource
	 */
	protected abstract void generateSourceFiles(File resource);
	
	/**
	 * 
	 * @param filename
	 * @param paths
	 * @return
	 */
	protected String createFilePath(String filename, String... paths) {
		StringBuilder result = new StringBuilder();
		for (String path : paths) {
			result.append(path).append(File.separator);
		}
		return result.append(filename).toString();
	}
	
	/**
	 * 
	 * @param output
	 * @param content
	 */
	protected void writeFile(File output, String content) {
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(output));
			writer.write(content);
			writer.flush();
		} catch (Exception e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @param templateName
	 * @return
	 */
	private String getTemplatePath(String templateName) {
		String templatePath = "/"+ getClass().getPackage().getName().replace('.', '/') +"/"+ templateName;
		URL templateURL = getClass().getResource(templatePath);
		if (templateURL == null) {
			throw new RuntimeException("Unable to find template "+ templatePath);
		}
		return templateURL.getFile();
	}

	/**
	 * 
	 * @param templateName
	 * @return
	 */
	protected String getTemplateContent(String templateName) {
		String result = templates.get(templateName);
		if (result == null) {
			BufferedReader reader = null;
			StringBuilder javaContent = new StringBuilder();
			try {
				reader = new BufferedReader(new FileReader(new File(getTemplatePath(templateName))));
				String line;
				while ((line = reader.readLine()) != null) {
					javaContent.append(line).append("\r\n");
				}
				result = javaContent.toString();
			} catch (Exception e) {
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return result;
	}
}
