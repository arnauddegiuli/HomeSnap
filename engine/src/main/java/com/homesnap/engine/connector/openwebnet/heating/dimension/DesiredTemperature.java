package com.homesnap.engine.connector.openwebnet.heating.dimension;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.impl.DoubleValue;

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

public class DesiredTemperature extends DimensionStatusImpl<DoubleValue> {

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
		// TODO gérer le nom en fonction de ca???
		return getIntValue(MODE_POS);
	}
	
	public void setMode(int mode) {
		// Check mode
		setIntValue(mode, MODE_POS, 1);
	}

	@Override
	public DoubleValue getStateValue() {
		return new DoubleValue(getDesiredTemperature());
	}

	@Override
	public void setValueList(DoubleValue value) {
		setDesiredTemperature(value.getDoubleValue());
	}
	
}
