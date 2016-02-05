package com.homesnap.engine.controller.what.impl;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
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

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.controller.what.State;

public class DateState implements State<Date> {

	public static String DATE_PATTERN = "dd-MM-yyyy";
	private Date value;
	
	public DateState(Date value) {
		setValue(value);		
	}

	@Override
	public void setValue(Date value) {
		this.value = value;
	}

	@Override
	public Date getValue() {
		return value;
	}

	@Override
	public void fromString(String value) {
		try {
			this.value = new SimpleDateFormat(DATE_PATTERN).parse(value);
		} catch (ParseException e) {
			new Log().fine(Session.Monitor, e.getMessage());
		}
	}

	@Override
	public String toString() {
		return new SimpleDateFormat(DATE_PATTERN).format(value);
	}

}
