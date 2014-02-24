package com.homesnap.engine.controller.gateway.dimension;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.homesnap.engine.controller.what.StateValue;

public class DateValue implements StateValue {
	
	public DateValue() {
	}
	
	public Date getDate() {
		return null;
	}
	
	public void setDate(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
