package com.homesnap.engine.controller.clock;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.gateway.dimension.DateValue;
import com.homesnap.engine.controller.what.StateName;
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
		return null;
	}
}
