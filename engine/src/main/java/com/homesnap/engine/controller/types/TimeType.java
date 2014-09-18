package com.homesnap.engine.controller.types;

import java.util.Locale;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class TimeType extends DateTimeType {
	
	/** */
	public static final String DEFAULT_PATTERN = "HH:mm:ss";
	
	/**
	 * 
	 */
	public TimeType() {
		super(Locale.getDefault(), DEFAULT_PATTERN);
	}
	
	/**
	 * 
	 * @param locale
	 * @param pattern
	 */
	public TimeType(Locale locale, String pattern) {
		super(locale, pattern);
	}
}
