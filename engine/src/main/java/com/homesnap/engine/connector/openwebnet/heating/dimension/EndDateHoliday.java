package com.homesnap.engine.connector.openwebnet.heating.dimension;

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

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.impl.DateState;


public class EndDateHoliday extends DimensionStatusImpl<DateState> {
	private int DAYOFWEEK_POS = 0;
	private int DAY_POS = 1;
	private int MONTH_POS = 2;
	private int YEAR_POS = 3;
	
	public EndDateHoliday() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Day of week
				new DimensionValueImpl(), // Day
				new DimensionValueImpl(), // Month
				new DimensionValueImpl()  // Year
				},
				"0"
		);
	}
	
	public java.util.Date getDate() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_WEEK, getIntValue(DAYOFWEEK_POS));
		c.set(Calendar.DAY_OF_MONTH, getIntValue(DAY_POS));
		c.set(Calendar.MONTH, getIntValue(MONTH_POS));
		c.set(Calendar.YEAR, getIntValue(YEAR_POS));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public void setTime(java.util.Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		setIntValue(c.get(Calendar.DAY_OF_WEEK), DAYOFWEEK_POS, 2);
		setIntValue(c.get(Calendar.DAY_OF_MONTH), DAY_POS, 2);
		setIntValue(c.get(Calendar.MONTH), MONTH_POS, 2);
		setIntValue(c.get(Calendar.YEAR), YEAR_POS, 4);
	}

	@Override
	public DateState getStateValue() {
		return new DateState(getDate());
	}

	@Override
	public void setStateValue(StateValue value) {
		setTime(((DateState)value).getDate());
	}
}
