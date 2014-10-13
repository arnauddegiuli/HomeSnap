package com.homesnap.engine.controller.properties;

import com.homesnap.engine.configuration.Property;
import com.homesnap.engine.controller.types.DateTimeType;
import com.homesnap.engine.controller.types.DateType;
import com.homesnap.engine.controller.types.LabelType;
import com.homesnap.engine.controller.types.ListOfValuesType;
import com.homesnap.engine.controller.types.NumberType;
import com.homesnap.engine.controller.types.PercentageType;
import com.homesnap.engine.controller.types.RGBType;
import com.homesnap.engine.controller.types.TimeType;
import com.homesnap.engine.controller.what.StateValueType;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class StateProperty implements Property {
	
	/** */
	private String name;
	
	/** */
	private String value;
	
	/** */
	private StateValueType type;
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	StateProperty(String name, String value) {
		this.name = name;
		this.value = value;
		parseType();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	/**
	 * @return the type
	 */
	public StateValueType getType() {
		return type;
	}

	/**
	 * 
	 * @throws UnknowStateValueTypeException 
	 */
	private void parseType() {
		if (value.length() == 0) {
			throw new RuntimeException("Value is empty for state name "+ name);
		}
		int length = value.length();
		char firstChar = value.charAt(0);
		if (firstChar == '#' && length > 2) { // Class type
			if (length > 2) {
				String keyword = value.substring(1);
				if ("Date".equals(keyword)) {
					type = new DateType();
				}
				else if ("Datetime".equals(keyword)) {
					type = new DateTimeType();
				}
				else if ("Label".equals(keyword)) {
					type = new LabelType();
				}
				else if ("Number".equals(keyword)) {
					type = new NumberType();
				}
				else if ("Percentage".equals(keyword)) {
					type = new PercentageType();
				}
				else if ("RGBType".equals(keyword)) {
					type = new RGBType();
				}
				else if ("Time".equals(keyword)) {
					type = new TimeType();
				} else {
					throw new UnknowStateValueTypeException("Unknown state value type "+ keyword +" for state name "+ name);
				}
			} else {
				throw new NullPointerException("Missing value after class definition character '#' for state name "+ name);
			}
		}
		else if (firstChar == '{') { // List of values
			if (length > 2 && (value.charAt(length-1) == '}')) {
				String values = value.substring(1, length-1);
				type = new ListOfValuesType(values.split(","));
			} else {
				throw new NullPointerException("Missing end character '}' of list of values for state name "+ name);
			}
		}
		else if (firstChar == '[') { // MinMaxType
			if (length > 2 && (value.charAt(length-1) == ']')) {
				// TODO parse min max type
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing end character ']' of number interval for state name "+ name);
		} else {
			throw new IllegalStateException("Unsupported value "+ value +" for state name "+ name);
		}
	}

	@Override
	public String toString() {
		return getName() +"="+ getValue();
	}
}
