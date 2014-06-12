package com.homesnap.engine.controller.clock;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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


import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.impl.DateValue;
import com.homesnap.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ClockSensor extends Controller  {
	
	public enum ClockSensorStateName implements StateName {
		
		ALARM("alarm"),
		DATE("date"),
		TIME("time"),
		DATE_TIME("date-time");
		
		private String name;
		
		private ClockSensorStateName(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	@Override
	protected void initStateTypes() {
		declareState(ClockSensorStateName.ALARM, DateValue.class);
		declareState(ClockSensorStateName.DATE, DateValue.class);
		declareState(ClockSensorStateName.TIME, DateValue.class);
		declareState(ClockSensorStateName.DATE_TIME, DateValue.class);
	}

	@Override
	public Who getWho() {
		// TODO Auto-generated method stub
		return null;
	}
}
