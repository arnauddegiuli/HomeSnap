package com.homesnap.engine.controller.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.homesnap.engine.controller.what.StateValueType;

public class DateTimeType extends StateValueType {
	
	/** */
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/** */
	private Locale locale;
	
	/** */
	private String pattern;
	
	/** */
	private Date value;
	
	/**
	 * 
	 */
	public DateTimeType() {
		this(Locale.getDefault(), DEFAULT_PATTERN);
	}
	
	/**
	 * 
	 * @param locale
	 */
	public DateTimeType(Locale locale, String pattern) {
		this.locale = locale;
		this.pattern = pattern;
		value = new Date();
	}

	@Override
	public String getValue() {
		return new SimpleDateFormat(pattern, locale).format(value);
	}

	@Override
	protected void setValue(String value) {
		try {
			this.value = new SimpleDateFormat(pattern, locale).parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date "+ value +" has wrong format, must be : "+ pattern);
		}
	}
}
