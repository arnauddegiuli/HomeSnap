package com.homesnap.engine.connector.openwebnet.gateway.dimension;

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
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
import com.homesnap.engine.controller.what.impl.TimeValue;


public class Time extends DimensionStatusImpl<TimeValue> {
	
	private int HOURS_POS = 0;
	private int MINUTES_POS = 1;
	private int SECONDS_POS = 2;
	private int TIMEZONE_POS = 3;
	
	public Time() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Hours
				new DimensionValueImpl(), // Min
				new DimensionValueImpl(), // Seconds
				new DimensionValueImpl()  // Timezone
				},
				GatewayDimension.TIME.getCode()
		);
	}
	

	@Override
	public TimeValue getStateValue() {
		return new TimeValue(getIntValue(HOURS_POS), getIntValue(MINUTES_POS), getIntValue(SECONDS_POS), getTimeZoneValue(TIMEZONE_POS));
	}

	@Override
	public void setValueList(TimeValue value) {
		Calendar c = new GregorianCalendar();
		c.setTime(value.getTime());
		setIntValue(c.get(Calendar.HOUR), HOURS_POS, 2);
		setIntValue(c.get(Calendar.MINUTE), MINUTES_POS, 2);
		setIntValue(c.get(Calendar.SECOND), SECONDS_POS, 2);
		setTimeZoneValue(c.getTimeZone(), TIMEZONE_POS);
	}
}
