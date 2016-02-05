package com.homesnap.engine.controller.what.impl;

import java.text.ParseException;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.homesnap.engine.controller.what.State;

public class TimeState implements State<Date>{

	public static final String TIME_PATTERN = "hh:mm:ss";

	private int hours, minutes, seconds;
	private TimeZone tz;

	public TimeState(int hours, int minutes, int seconds, TimeZone tz) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		if (tz == null) {
			this.tz = TimeZone.getDefault();
		} else {
			this.tz = tz;
		}
	}

	public Date getTime() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR, hours);
		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.SECOND, seconds);
		c.set(Calendar.DAY_OF_MONTH, 0);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.YEAR, 0);
		c.setTimeZone(tz);
		return c.getTime();
	}
	
	public void setTime(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		hours = c.get(Calendar.HOUR);
		minutes = c.get(Calendar.MINUTE);
		seconds = c.get(Calendar.SECOND);
		tz = c.getTimeZone();
	}

	@Override
	public Date getValue() {
		return getTime();
	}

	@Override
	public void setValue(Date value) {
		setTime(value);
	}

	@Override
	public void fromString(String value) {
		try {
			setValue(new SimpleDateFormat(TIME_PATTERN).parse(value));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
