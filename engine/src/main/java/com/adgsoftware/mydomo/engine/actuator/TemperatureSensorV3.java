package com.adgsoftware.mydomo.engine.actuator;

import java.util.Map;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.actuator.what.DoubleValue;
import com.adgsoftware.mydomo.engine.actuator.what.core.StateName;
import com.adgsoftware.mydomo.engine.actuator.what.core.StateValue;
import com.adgsoftware.mydomo.engine.actuator.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 */
public class TemperatureSensorV3 extends Controller {
	
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
		@Override
		public JSONObject toJson() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void fromJson(JSONObject jsonObject) throws UnmarshalException {
			// TODO Auto-generated method stub
			
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
		@Override
		public JSONObject toJson() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void fromJson(JSONObject jsonObject) throws UnmarshalException {
			// TODO Auto-generated method stub
			
		}

	}

	public void setUnit(TemperatureUnitType unit) {
		set(StateName.UNIT, unit);
	}
	
	public TemperatureSensorV3() {
		super();
	}
	
	public void setAlarmHigh(double alarm) {
		set(StateName.ALARM_HIGH, new DoubleValue(alarm));
	}
	
	public void setAlarmLow(double alarm) {
		set(StateName.ALARM_LOW, new DoubleValue(alarm));
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
		// TODO Auto-generated method stub
		return null;
	}
}
