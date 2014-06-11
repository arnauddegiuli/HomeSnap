package com.homesnap.engine.connector.openwebnet.heating.dimension;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.homesnap.engine.controller.what.impl.DoubleValue;

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


public class MeasureTemperature extends DimensionStatusImpl<DoubleValue> {

	private int TEMPERATURE_POS = 0;
	
	public MeasureTemperature() {
		super(new DimensionValue[] { 
				new DimensionValueImpl() // T: temperature not adjust by local offset. Composed by 4 digit: c1c2c3c4 included between "0050" (5° temperature) and "0400" (40° temperature). c1 is always equals to 0, it indicates a positive temperature. The c2c3 couple indicates the temperature between [05°-40°]. c4 indicates the decimal Celsius degree by 0.5° step.
				},
				HeatingZoneDimension.MEASURE_TEMPERATURE.getCode()
		);
	}

	public Double getMeasuredTemperature() {
		return getTemperatureValue(TEMPERATURE_POS);
	}
	
	public void setMeasuredTemperature(Double value) {
		setTemperatureValue(value, TEMPERATURE_POS);
	}

	@Override
	public DoubleValue getStateValue() {
		return new DoubleValue(getMeasuredTemperature());
	}

	@Override
	public void setValueList(DoubleValue value) {
		setMeasuredTemperature(value.getDoubleValue());
	}
}
