package com.homesnap.engine.connector.openwebnet.heating.dimension;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;

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

public class DesiredTemperature extends DimensionStatusImpl {

	private int TEMPERATURE_POS = 0;
	private int MODE_POS = 1;
	
	public DesiredTemperature() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // T: temperature not adjust by local offset. Composed by 4 digit: c1c2c3c4 included between "0050" (5° temperature) and "0400" (40° temperature). c1 is always equals to 0, it indicates a positive temperature. The c2c3 couple indicates the temperature between [05°-40°]. c4 indicates the decimal Celsius degree by 0.5° step.
				new DimensionValueImpl()  // M: Operation Mode (1: heating mode, 2: conditional mode, 3: generic mode)
				},
				HeatingZoneDimension.SET_TEMPERATURE.getCode()
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
