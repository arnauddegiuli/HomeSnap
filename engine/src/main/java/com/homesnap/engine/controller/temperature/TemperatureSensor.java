package com.homesnap.engine.controller.temperature;

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
