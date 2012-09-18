package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class DateTime extends DimensionStatusImpl {
	
	private int HOURS_POS = 0;
	private int MINUTES_POS = 1;
	private int SECONDS_POS = 2;
	private int TIMEZONE_POS = 3;
	private int DAYOFWEEK_POS = 4;
	private int DAY_POS = 5;
	private int MONTH_POS = 6;
	private int YEAR_POS = 7;
	
	public DateTime() {
		super(new DimensionValue[] { 
				
				new DimensionValueImpl(), // Hours
				new DimensionValueImpl(), // Minute
				new DimensionValueImpl(), // Second
				new DimensionValueImpl(), // Timezone
				new DimensionValueImpl(), // Day of week
				new DimensionValueImpl(), // Day
				new DimensionValueImpl(), // Month
				new DimensionValueImpl()  // Year
				},
			Gateway.GatewayDimension.DATETIME.getCode()
		);
	}
	
	public java.util.Date getDate() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_WEEK, getIntValue(DAYOFWEEK_POS));
		c.set(Calendar.DAY_OF_MONTH, getIntValue(DAY_POS));
		c.set(Calendar.MONTH, getIntValue(MONTH_POS));
		c.set(Calendar.YEAR, getIntValue(YEAR_POS));
		c.set(Calendar.HOUR, getIntValue(HOURS_POS));
		c.set(Calendar.MINUTE, getIntValue(MINUTES_POS));
		c.set(Calendar.SECOND, getIntValue(SECONDS_POS));
		c.setTimeZone(getTimeZoneValue(TIMEZONE_POS));
		
		return c.getTime();
	}
	
	public void setTime(java.util.Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		setIntValue(c.get(Calendar.DAY_OF_WEEK), DAYOFWEEK_POS, 2);
		setIntValue(c.get(Calendar.DAY_OF_MONTH), DAY_POS, 2);
		setIntValue(c.get(Calendar.MONTH), MONTH_POS, 2);
		setIntValue(c.get(Calendar.YEAR), YEAR_POS, 4);
		setIntValue(c.get(Calendar.HOUR), HOURS_POS, 2);
		setIntValue(c.get(Calendar.MINUTE), MINUTES_POS, 2);
		setIntValue(c.get(Calendar.SECOND), SECONDS_POS, 2);
		setTimeZoneValue(c.getTimeZone(), TIMEZONE_POS);
	}
}
