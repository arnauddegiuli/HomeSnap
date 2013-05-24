package com.adgsoftware.mydomo.engine.controller.heating.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;

public class DesiredTemperature extends DimensionStatusImpl {

	private int TEMPERATURE_POS = 0;
	private int MODE_POS = 1;
	
	public DesiredTemperature() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // T: temperature not adjust by local offset. Composed by 4 digit: c1c2c3c4 included between "0050" (5° temperature) and "0400" (40° temperature). c1 is always equals to 0, it indicates a positive temperature. The c2c3 couple indicates the temperature between [05°-40°]. c4 indicates the decimal Celsius degree by 0.5° step.
				new DimensionValueImpl()  // M: Operation Mode (1: heating mode, 2: conditional mode, 3: generic mode)
				},
				HeatingZone.HeatingZoneDimension.SET_TEMPERATURE.getCode()
		);
	}

	public Double getDesiredTemperature() {
		return getTemperatureValue(TEMPERATURE_POS);
	}
	
	public void setDesiredTemperature(Double temperature) {
		setTemperatureValue(temperature, TEMPERATURE_POS);
	}
	
	public int getMode() {
		return getIntValue(MODE_POS);
	}
	
	public void setMode(int mode) {
		// Check mode
		setIntValue(mode, MODE_POS, 1);
	}
	
}
