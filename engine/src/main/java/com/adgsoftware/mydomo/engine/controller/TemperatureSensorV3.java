package com.adgsoftware.mydomo.engine.controller;

import java.util.Map;

import com.adgsoftware.mydomo.engine.controller.what.StateName;
import com.adgsoftware.mydomo.engine.controller.what.StateValue;
import com.adgsoftware.mydomo.engine.controller.what.impl.DoubleValue;
import com.adgsoftware.mydomo.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 */
public class TemperatureSensorV3 extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TemperatureSensorStateName implements StateName {
		UNIT("unit"),
		ALARM_HIGH("alarm_high"),
		ALARM_LOW("alarm_low");

		private String name;
		
		private TemperatureSensorStateName(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
		
	}

	public enum TemperatureStatus implements StateValue {
		ACTIVE("active"),
		INACTIVE("inactive");
		private String value;
		private TemperatureStatus(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	public enum TemperatureUnitType implements StateValue {
		CELSIUS("C°"),
		FAHRENHEIT("F°");		
		private String value;
		private TemperatureUnitType(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}

	}

	public void setUnit(TemperatureUnitType unit) {
		set(TemperatureSensorStateName.UNIT, unit);
	}
	
	public TemperatureSensorV3() {
		super();
	}
	
	public void setAlarmHigh(double alarm) {
		set(TemperatureSensorStateName.ALARM_HIGH, new DoubleValue(alarm));
	}
	
	public void setAlarmLow(double alarm) {
		set(TemperatureSensorStateName.ALARM_LOW, new DoubleValue(alarm));
	}
	
	public static void main(String[] args) {
		
		TemperatureSensorV3 sensor = new TemperatureSensorV3();
		
		sensor.setUnit(TemperatureUnitType.CELSIUS);
		sensor.setAlarmHigh(28.35);
		sensor.setAlarmLow(12.5);
		
	}

	@Override
	protected Map<StateName, Class<? extends StateValue>> getSupportedStateTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Who getWho() {
		return Who.HEATING_ADJUSTMENT;
	}
}
