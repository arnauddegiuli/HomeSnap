package com.homesnap.engine.controller.types;

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
	public void setValue(String value) {
		try {
			this.value = new SimpleDateFormat(pattern, locale).parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date "+ value +" has wrong format, must be : "+ pattern);
		}
	}
}
