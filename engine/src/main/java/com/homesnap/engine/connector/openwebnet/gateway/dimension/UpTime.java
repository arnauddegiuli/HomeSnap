package com.homesnap.engine.connector.openwebnet.gateway.dimension;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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
import java.util.Date;
import java.util.GregorianCalendar;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimensionConverter;
import com.homesnap.engine.controller.what.StateValue;
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
			GatewayDimensionConverter.UPTIME.getCode()
		);
	}
	
	private Date getTime() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_YEAR, getIntValue(DAY_POS));
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
	public void setStateValue(StateValue value) {
		// TODO throw new ReadOnlyException(); // read only dimension
	}

	public void setTime(java.util.Date time) {
		Calendar c = new GregorianCalendar();
		c.setTime(time);
		setIntValue(c.get(Calendar.DAY_OF_YEAR), DAY_POS, 2);
		setIntValue(c.get(Calendar.HOUR), HOURS_POS, 2);
		setIntValue(c.get(Calendar.MINUTE), MINUTES_POS, 2);
		setIntValue(c.get(Calendar.SECOND), SECONDS_POS, 2);
	}
}
