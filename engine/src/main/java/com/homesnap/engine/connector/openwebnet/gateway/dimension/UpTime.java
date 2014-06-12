package com.homesnap.engine.connector.openwebnet.gateway.dimension;

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

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
import com.homesnap.engine.controller.what.impl.DateValue;


public class UpTime extends DimensionStatusImpl<DateValue> {
	
	private int DAY_POS = 0;
	private int HOURS_POS = 1;
	private int MINUTES_POS = 2;
	private int SECONDS_POS = 3;
	
	public UpTime() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Day
				new DimensionValueImpl(), // Hours
				new DimensionValueImpl(), // Min
				new DimensionValueImpl(), // Seconds
				},
			GatewayDimension.UPTIME.getCode()
		);
	}
	
	private Date getTime() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_MONTH, getIntValue(DAY_POS));
		c.set(Calendar.HOUR, getIntValue(HOURS_POS));
		c.set(Calendar.MINUTE, getIntValue(MINUTES_POS));
		c.set(Calendar.SECOND, getIntValue(SECONDS_POS));
		
		return c.getTime();
	}

	@Override
	public DateValue getStateValue() {
		return new DateValue(getTime());
	}

	@Override
	public void setValueList(DateValue value) {
		// TODO normalement impossible => lecture seule
		
	}
}
