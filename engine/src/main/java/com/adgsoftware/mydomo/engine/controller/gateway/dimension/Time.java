package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class Time extends DimensionStatusImpl {
	
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
				Gateway.GatewayDimension.TIME.getCode()
		);
	}
	
	public Date getTime() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR, getIntValue(HOURS_POS));
		c.set(Calendar.MINUTE, getIntValue(MINUTES_POS));
		c.set(Calendar.SECOND, getIntValue(SECONDS_POS));
		c.setTimeZone(getTimeZoneValue(TIMEZONE_POS));
		c.set(Calendar.DAY_OF_MONTH, 0);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.YEAR, 0);
		return c.getTime();
	}
	
	public void setTime(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		setIntValue(c.get(Calendar.HOUR), HOURS_POS, 2);
		setIntValue(c.get(Calendar.MINUTE), MINUTES_POS, 2);
		setIntValue(c.get(Calendar.SECOND), SECONDS_POS, 2);
		setTimeZoneValue(c.getTimeZone(), TIMEZONE_POS);
	}
}
