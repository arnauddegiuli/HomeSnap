package com.homesnap.engine.controller.types;

import java.util.Locale;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class DateType extends DateTimeType {
	
	/** */
	public static final String DEFAULT_PATTERN = "yyyy-mm-dd";
	
	/**
	 * 
	 */
	public DateType() {
		super(Locale.getDefault(), DEFAULT_PATTERN);
	}
	
	/**
	 * 
	 * @param locale
	 */
	public DateType(Locale locale, String pattern) {
		super(locale, pattern);
	}
}
