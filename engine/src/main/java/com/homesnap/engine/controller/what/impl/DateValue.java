package com.homesnap.engine.controller.what.impl;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.text.SimpleDateFormat;
import java.util.Date;

import com.homesnap.engine.controller.what.StateValue;

public class DateValue implements StateValue {

	private String DATE_PATTERN = "dd-MM-yyyy";
	
	/** */
	private Date value;
	
	/**
	 * 
	 * @param value
	 */
	public DateValue(Date value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return new SimpleDateFormat(DATE_PATTERN).format(value);
	}

	public Date getDate() {
		return value;
	}
}
