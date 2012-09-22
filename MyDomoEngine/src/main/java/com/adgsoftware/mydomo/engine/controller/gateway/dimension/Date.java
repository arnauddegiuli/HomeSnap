package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class Date extends DimensionStatusImpl {
	
	private int DAYOFWEEK_POS = 0;
	private int DAY_POS = 1;
	private int MONTH_POS = 2;
	private int YEAR_POS = 3;
	
	public Date() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Day of week
				new DimensionValueImpl(), // Day
				new DimensionValueImpl(), // Month
				new DimensionValueImpl()  // Year
				},
				Gateway.GatewayDimension.DATE.getCode()
		);
	}
	
	public java.util.Date getDate() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_WEEK, getIntValue(DAYOFWEEK_POS));
		c.set(Calendar.DAY_OF_MONTH, getIntValue(DAY_POS));
		c.set(Calendar.MONTH, getIntValue(MONTH_POS));
		c.set(Calendar.YEAR, getIntValue(YEAR_POS));
		// TODO set time to 0
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
}
