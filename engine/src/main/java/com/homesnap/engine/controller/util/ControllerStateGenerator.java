package com.homesnap.engine.controller.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.StateProperties;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;

/**
 * Utility class to generate {@link StateValue} enum classes for user-defined controllers.
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ControllerStateGenerator {
	
	/** Location of the HomeSnap source files */
	private String sourcePath;
	
	/** Location of the HomeSnap binaries */
	private String classPath;
	
	/** Location of the java enumeration state name template */
	private String templatePath;
	
	/** Content of the state name template */
	private String templateContent;
	
	/** Indicates if the java enumeration state files must be overwritten */
	private boolean forceUpdate;
	
	private static final String EOL = "\r\n";
	
	/**
	 * 
	 */
	public ControllerStateGenerator() {
	}
	
	/**
	 * 
	 */
	public void generateEnumerations() {
		generateEnumerations(false);
	}
	
	/**
	 * 
	 * @param forceUpdate
	 */
	public void generateEnumerations(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
		
		// Load the template resource to determine if the HomeSnap distribution is locally unzipped
		String packagePath = getClass().getPackage().getName().replace('.', '/');
		templatePath = "/"+ packagePath +"/stateNameEnumTemplate";
		URL templateURL = getClass().getResource("/"+ packagePath +"/stateNameEnumTemplate");
		if (templateURL == null) {
			throw new RuntimeException("Unable to find template resource "+ templatePath);
		}
		if (!"file".equals(templateURL.getProtocol())) {
			throw new RuntimeException("Unable to generate state files outside local opensource distribution. Download source code from http://");
		}
		templatePath = new File(templateURL.getFile()).getAbsolutePath();
		
		// Find the root folder for classes
		URL basePath = getClass().getResource("/");
		if (basePath == null) {
			throw new RuntimeException("Unable to find resource folder.");
		}
		classPath = basePath.getFile();
		classPath = new File(classPath).getAbsolutePath();
		
		// Find the source folder
		sourcePath = new File(classPath.substring(0, (classPath.length() - "target/classes/".length())).concat("/src/main/java/")).getAbsolutePath();
		if (sourcePath == null) {
			throw new RuntimeException("Unable to find source folder "+ sourcePath);
		}
		generateSourceFiles(new File(sourcePath));
	}
	
	/**
	 * 
	 * @param sourceFolder
	 */
	private void generateSourceFiles(File sourceFolder) {
		for (File f : sourceFolder.listFiles()) {
			if (f.isDirectory()) {
				generateSourceFiles(f);
			}
			else if (f.getName().endsWith(Controller.STATES_EXTENSION)) {
				generateStateNameClass(f);
			}
        }
	}

	/**
	 * 
	 * @param sourceStateFile
	 */
	private void generateStateNameClass(File sourceStateFile) {
		
		String packagePath = sourceStateFile.getParentFile().getAbsolutePath().substring(classPath.length());
		String controllerClassName = sourceStateFile.getName().substring(0, (sourceStateFile.getName().length() - Controller.STATES_EXTENSION.length()));
		String stateNameClass = controllerClassName.concat(StateName.class.getSimpleName());
		
		// Determines if the states definition file (.states) located in the source path is more recent than the java source file which defines the state names of the controller.
		File javaSourceFile = new File(createFilePath(stateNameClass +".java", sourcePath, packagePath));
		if (!forceUpdate && javaSourceFile.exists() && javaSourceFile.lastModified() > sourceStateFile.lastModified()) {
			return;
		}
		// Load the definition file
		StateProperties stateDefinition = new StateProperties();
		try {
			stateDefinition.load(sourceStateFile);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load states definition file "+ sourceStateFile.getAbsolutePath(), e);
		}
		// Checks if the "generate_once" property is set to "true" to not overwrite java enum class if exists
		if (stateDefinition.mustBeGeneratedOnce() && javaSourceFile.exists()) {
			return;
		}
		// Initialize the java constants
		List<String> stateNames = new ArrayList<String>(stateDefinition.getSectionProperties(StateProperties.CONTROLLER_SECTION).keySet());
		stateNames.sort(null);
		StringBuilder enumValues = new StringBuilder();
		String separator = ","+ EOL;
		for (String stateName : stateNames) {
			enumValues.append("\t").append(generateJavadoc(stateDefinition, stateName)).append(EOL)
				.append("\t").append(stateName.toUpperCase()).append(separator);
		}
		enumValues.setLength(enumValues.length() - separator.length());
		enumValues = enumValues.append(";");
		
		// Generates the java enumeration class file which defines all state names of the controller.
		String javaContent = getTemplateContent();
		javaContent = javaContent.replace("@PACKAGE_NAME@", packagePath.replace(File.separatorChar, '.'));
		javaContent = javaContent.replace("@STATE_NAME_CLASS@", StateName.class.getName());
		javaContent = javaContent.replace("@CONTROLER_NAME@", controllerClassName);
		javaContent = javaContent.replace("@GENERATOR_NAME@", getClass().getSimpleName());
		javaContent = javaContent.replace("@ENUMERATION_NAME@", stateNameClass);
		javaContent = javaContent.replace("@STATE_NAME_CLASSNAME@", StateName.class.getSimpleName());
		javaContent = javaContent.replace("@STATE_NAME_VALUES@", enumValues.toString());
		writeEnumFile(javaSourceFile, javaContent);
	}
	
	/**
	 * 
	 * @param stateDefinition
	 * @param stateName
	 * @return
	 */
	private String generateJavadoc(StateProperties stateDefinition, String stateName) {
		
		String javadoc = stateDefinition.getSectionProperty(StateProperties.DOCUMENTATION_SECTION, stateName);
		StringBuilder result = new StringBuilder("/** ");
		if (javadoc == null) {
			javadoc = "State name "+ stateName;
		}
		return result.append(javadoc).append(" */").toString();
	}
	
	/**
	 * 
	 * @param output
	 * @param content
	 */
	private void writeEnumFile(File output, String content) {
		
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
	 * @return
	 */
	private String getTemplateContent() {
		if (templateContent == null) {
			BufferedReader reader = null;
			StringBuilder javaContent = new StringBuilder();
			try {
				reader = new BufferedReader(new FileReader(new File(templatePath)));
				String line;
				while ((line = reader.readLine()) != null) {
					javaContent.append(line).append("\r\n");
				}
				templateContent = javaContent.toString();
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
		return templateContent;
	}
	
	/**
	 * 
	 * @param filename
	 * @param paths
	 * @return
	 */
	private String createFilePath(String filename, String... paths) {
		StringBuilder result = new StringBuilder();
		for (String path : paths) {
			result.append(path).append(File.separator);
		}
		return result.append(filename).toString();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new ControllerStateGenerator().generateEnumerations();
	}
}