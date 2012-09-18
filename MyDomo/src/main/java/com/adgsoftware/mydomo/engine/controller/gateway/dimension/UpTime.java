package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class UpTime extends DimensionStatusImpl {
	
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
			Gateway.GatewayDimension.UPTIME.getCode()
		);
	}
	
	public Date getTime() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_MONTH, getIntValue(DAY_POS));
		c.set(Calendar.HOUR, getIntValue(HOURS_POS));
		c.set(Calendar.MINUTE, getIntValue(MINUTES_POS));
		c.set(Calendar.SECOND, getIntValue(SECONDS_POS));
		
		return c.getTime();
	}
}
