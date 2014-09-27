package com.homesnap.engine.controller.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.UnknowStateValueTypeException;
import com.homesnap.engine.controller.types.ListOfValuesType;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateProperties;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.StateValueType;

/**
 * Utility class to generate {@link StateValue} enum classes for user-defined controllers.
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ControllerStateGenerator {
	
	/** Location of the output path for generated java source files */
	private String outputPath;
	
	/** Location of the HomeSnap binaries */
	private String classPath;
	
	/** List of templates names with their location */
	private Map<String, String> templateNames = new HashMap<String, String>();
	
	/** List of templates names with their content */
	private Map<String, String> templates = new HashMap<String, String>();
	
	/** Indicates if the java enumeration state files must be overwritten */
	private boolean forceUpdate;
	
	private static final String EOL = "\r\n";
	private static final String STATE_NAME_TEMPLATE = "stateNameEnumTemplate";
	private static final String LIST_OF_VALUES_TEMPLATE = "listOfValuesEnumTemplate";
	
	/**
	 * 
	 */
	public ControllerStateGenerator() {
	}
	
	/**
	 * 
	 * @param outputPath
	 */
	public void generateEnumerations(String outputPath) {
		generateEnumerations(outputPath, false);
	}
	
	/**
	 * 
	 * @param outputPath
	 * @param forceUpdate
	 */
	public void generateEnumerations(String outputPath, boolean forceUpdate) {
		
		this.forceUpdate = forceUpdate;
		
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
		
		// Load path of template resources
		loadTemplatePath(STATE_NAME_TEMPLATE);
		loadTemplatePath(LIST_OF_VALUES_TEMPLATE);
		
//		String templatePath = templateNames.get(STATE_NAME_TEMPLATE);
//		URL templateURL = getClass().getResource(templatePath);
//		if (templateURL == null) {
//			throw new RuntimeException("Unable to find template resource "+ templatePath);
//		}
//		if (!"file".equals(templateURL.getProtocol())) {
//			throw new RuntimeException("Unable to generate state files outside local opensource distribution. Download source code from http://");
//		}
//		templatePath = new File(templateURL.getFile()).getAbsolutePath();
		
		generateEnumerations(sourceFolder);
	}
	
	/**
	 * 
	 * @param stateNameTemplate
	 */
	private void loadTemplatePath(String stateNameTemplate) {
		String templatePath = "/"+ getClass().getPackage().getName().replace('.', '/') +"/"+ stateNameTemplate;
		URL templateURL = getClass().getResource(templatePath);
		if (templateURL == null) {
			throw new RuntimeException("Unable to load template "+ templatePath);
		}
		templateNames.put(stateNameTemplate, templateURL.getFile());
	}
	
	/**
	 * 
	 * @param sourceFolder
	 */
	private void generateEnumerations(File sourceFolder) {
		for (File stateFile : sourceFolder.listFiles()) {
			if (stateFile.isDirectory()) {
				generateEnumerations(stateFile);
			}
			else if (stateFile.getName().endsWith(Controller.STATES_EXTENSION)) {
				generateSourceFiles(stateFile);
			}
        }
	}
	
	/**
	 * 
	 * @param stateFile
	 * @throws UnknowStateValueTypeException 
	 */
	private void generateSourceFiles(File stateFile) {
		
		String packagePath = stateFile.getParentFile().getAbsolutePath().substring(classPath.length());
		String controllerClassName = stateFile.getName().substring(0, (stateFile.getName().length() - Controller.STATES_EXTENSION.length()));
		String stateNameClass = controllerClassName.concat(StateName.class.getSimpleName());
		Map<String, String> tokens = new HashMap<String, String>();
		
		// Determines if the states definition file (.states) located in the source path is more recent than the java source file which defines the state names of the controller.
		File javaSourceFile = new File(createFilePath(stateNameClass +".java", outputPath, packagePath));
		if (!forceUpdate && javaSourceFile.exists() && javaSourceFile.lastModified() > stateFile.lastModified()) {
			return;
		}
		// Load the definition file
		StateProperties stateDefinition = new StateProperties();
		try {
			stateDefinition.load(stateFile);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load states definition file "+ stateFile.getAbsolutePath(), e);
		}
		// Checks if the "generate_once" property is set to "true" to not overwrite java enum class if exists
		if (stateDefinition.mustBeGeneratedOnce() && javaSourceFile.exists()) {
			return;
		}
		// Orders the state names by name
		List<String> stateNames = new ArrayList<String>(stateDefinition.getSectionProperties(StateProperties.CONTROLLER_SECTION).keySet());
		Arrays.sort(stateNames.toArray());
		StringBuilder enumValues = new StringBuilder();
		String separator = ","+ EOL;
		
		tokens.put("@PACKAGE_NAME@", packagePath.replace(File.separatorChar, '.'));
		tokens.put("@STATE_VALUE_CLASS@", StateValue.class.getName());
		tokens.put("@GENERATOR_NAME@", getClass().getSimpleName());
		tokens.put("@STATE_VALUE_CLASSNAME@", StateValue.class.getSimpleName());
		
		for (String stateName : stateNames) {
			enumValues.append("\t").append(generateJavadoc(stateDefinition, stateName)).append(EOL)
				.append("\t").append(stateName.toUpperCase()).append(separator);
			
			// If the state value's type is a list of values, generates the java enumeration class which defines all possible values of the state name.
			StateValueType type = null;
			try {
				type = stateDefinition.getStateValueType(stateName);
			} catch (UnknowStateValueTypeException e) {
				// User defined types
			}
			if (type != null && type instanceof ListOfValuesType) {
				
				String stateValueClass = new StringBuilder(controllerClassName)
					.append(stateName.substring(0, 1).toUpperCase()).append(stateName.substring(1))
					.append(StateValue.class.getSimpleName()).toString();
				
				tokens.put("@STATE_NAME_CONSTANT@", generateStateNameLink(stateNameClass, stateName));
				tokens.put("@ENUMERATION_NAME@", stateValueClass);
				tokens.put("@STATE_VALUE_VALUES@", splitValues(((ListOfValuesType) type).getValues(), separator));
				generateEnumerationClass(new File(createFilePath(stateValueClass +".java", outputPath, packagePath)), LIST_OF_VALUES_TEMPLATE, tokens);
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
		writeEnumFile(output, javaContent);
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
	 * @param templateName
	 * @return
	 */
	private String getTemplateContent(String templateName) {
		String result = templates.get(templateName);
		if (result == null) {
			BufferedReader reader = null;
			StringBuilder javaContent = new StringBuilder();
			try {
				reader = new BufferedReader(new FileReader(new File(templateNames.get(templateName))));
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

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String srcDir = null;
		if (args.length == 1) {
			srcDir = args[0];
		}
		new ControllerStateGenerator().generateEnumerations(srcDir);
	}
}