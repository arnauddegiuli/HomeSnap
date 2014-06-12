package com.homesnap.engine.controller.temperature;

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


import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.impl.DoubleValue;
import com.homesnap.engine.controller.what.impl.StringValue;
import com.homesnap.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class TemperatureSensor extends Controller {
	
	/** */
	private static final long serialVersionUID = 5006236684338587464L;

	public enum TemperatureSensorStateName implements StateName {
		
		ALARM_HIGH("alarm-high"),
		ALARM_LOW("alarm-low"),
		HIGHEST_TEMP("highest-temp"),
		LOWEST_TEMP("lowest-temp"),
		PRECISION("precision"),
		VALUE("value"),
		UNIT("unit");
		
		private String name;
		
		private TemperatureSensorStateName(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	
	@Override
	protected void initStateTypes() {
		declareState(TemperatureSensorStateName.ALARM_HIGH, DoubleValue.class);
		declareState(TemperatureSensorStateName.ALARM_LOW, DoubleValue.class);
		declareState(TemperatureSensorStateName.PRECISION, DoubleValue.class);
		declareState(TemperatureSensorStateName.VALUE, DoubleValue.class);
		declareState(TemperatureSensorStateName.UNIT, StringValue.class);
	}

	@Override
	public Who getWho() {
		return Who.TEMPERATURE_SENSOR;
	}
}
