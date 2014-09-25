package com.homesnap.engine.controller.what;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import com.homesnap.engine.controller.UnknowStateValueTypeException;
import com.homesnap.engine.controller.types.DateTimeType;
import com.homesnap.engine.controller.types.DateType;
import com.homesnap.engine.controller.types.LabelType;
import com.homesnap.engine.controller.types.ListOfValuesType;
import com.homesnap.engine.controller.types.NumberType;
import com.homesnap.engine.controller.types.PercentageType;
import com.homesnap.engine.controller.types.RGBType;
import com.homesnap.engine.controller.types.TimeType;
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
	
	/**
	 * 
	 * @param stateName
	 * @return
	 * @throws UnknowStateValueTypeException
	 */
	public StateValueType getStateValueType(String stateName) throws UnknowStateValueTypeException {
		
		String value = getSectionProperty(CONTROLLER_SECTION, stateName);
		if (value.length() == 0) {
			// TODO ConfigurationException
			throw new RuntimeException("Value is empty for state name "+ stateName);
		}
		int length = value.length();
		char firstChar = value.charAt(0);
		if (firstChar == '#' && length > 2) { // Class type
			if (length > 2) {
				String keyword = value.substring(1);
				if ("Date".equals(keyword)) {
					return new DateType();
				}
				else if ("Datetime".equals(keyword)) {
					return new DateTimeType();
				}
				else if ("Label".equals(keyword)) {
					return new LabelType();
				}
				else if ("Number".equals(keyword)) {
					return new NumberType();
				}
				else if ("Percentage".equals(keyword)) {
					return new PercentageType();
				}
				else if ("RGBType".equals(keyword)) {
					return new RGBType();
				}
				else if ("Time".equals(keyword)) {
					return new TimeType();
				}
				throw new UnknowStateValueTypeException("Unknown state value type "+ keyword +" for state name "+ stateName);
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing value after class definition character '#' for state name "+ stateName);
		}
		else if (firstChar == '{') { // List of values
			if (length > 2 && (value.charAt(length-1) == '}')) {
				String values = value.substring(1, length-1);
				return new ListOfValuesType(values.split(","));
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing end character '}' of list of values for state name "+ stateName);
		}
		else if (firstChar == '[') { // MinMaxType
			if (length > 2 && (value.charAt(length-1) == ']')) {
				// TODO parse min max type
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing end character ']' of number interval for state name "+ stateName);
		}
		throw new IllegalStateException("Unsupported value "+ value +" for state name "+ stateName);
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
