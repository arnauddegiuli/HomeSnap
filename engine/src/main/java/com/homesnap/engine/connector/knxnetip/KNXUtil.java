package com.homesnap.engine.connector.knxnetip;

import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

import com.homesnap.engine.controller.light.Light.LightStateValue;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXUtil {

	/**
	 * 
	 * @param who
	 * @param value
	 * @return
	 */
	public static DPT getStateType(Who who) {
		
		DPT result = null;
		switch (who) {
			case AUTOMATION:
			case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			case GATEWAY:
			case HEATING_ADJUSTMENT:
			case MULTIMEDIA:
			case POWER_MANAGEMENT:
			case SCENARIO:
			case SOUND_SYSTEM: {
				break;
			}
				
			case LIGHT: {
				result = DPTXlatorBoolean.DPT_SWITCH;
				break;
			}
			default: {
			}
		}
		return result;
	}

	/**
	 * 
	 * @param who
	 * @param stateValue
	 * @return
	 */
	public static String getStateValueToRead(Who who, StateValue stateValue) {
		
		String result = null;
		switch (who) {
			case AUTOMATION:
			case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			case GATEWAY:
			case HEATING_ADJUSTMENT:
			case MULTIMEDIA:
			case POWER_MANAGEMENT:
			case SCENARIO:
			case SOUND_SYSTEM: {
				break;
			}
				
			case LIGHT: {
				result = LightStateValue.LIGHT_ON.equals(stateValue) ? "on" : LightStateValue.LIGHT_OFF.equals(stateValue) ? "off" : null;
				break;
			}
			default: {
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param who
	 * @param stateValue
	 * @return
	 */
	public static StateValue getStateValueToWrite(Who who, String value) {
		
		StateValue result = null;
		switch (who) {
			case AUTOMATION:
			case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			case GATEWAY:
			case HEATING_ADJUSTMENT:
			case MULTIMEDIA:
			case POWER_MANAGEMENT:
			case SCENARIO:
			case SOUND_SYSTEM: {
				break;
			}
				
			case LIGHT: {
				result = DPTXlatorBoolean.DPT_SWITCH.getLowerValue().equals(value) ? LightStateValue.LIGHT_ON : DPTXlatorBoolean.DPT_SWITCH.getUpperValue().equals(value) ? LightStateValue.LIGHT_OFF : null;
				break;
			}
			default: {
			}
		}
		return result;
	}
}
