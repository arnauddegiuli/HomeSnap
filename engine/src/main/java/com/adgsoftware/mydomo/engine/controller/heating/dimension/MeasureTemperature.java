package com.adgsoftware.mydomo.engine.controller.heating.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;

public class MeasureTemperature extends DimensionStatusImpl {

	private int TEMPERATURE_POS = 0;
	
	public MeasureTemperature() {
		super(new DimensionValue[] { 
				new DimensionValueImpl() // T: temperature not adjust by local offset. Composed by 4 digit: c1c2c3c4 included between "0050" (5° temperature) and "0400" (40° temperature). c1 is always equals to 0, it indicates a positive temperature. The c2c3 couple indicates the temperature between [05°-40°]. c4 indicates the decimal Celsius degree by 0.5° step.
				},
				HeatingZone.HeatingZoneDimension.MEASURE_TEMPERATURE.getCode()
		);
	}

	public Double getMeasuredTemperature() {
		return getTemperatureValue(TEMPERATURE_POS);
	}
	
	public void setMeasuredTemperature(Double value) {
		setTemperatureValue(value, TEMPERATURE_POS);
	}
}
