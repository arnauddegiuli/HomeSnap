package com.homesnap.engine.connector.knxnetip;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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


import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

import com.homesnap.engine.controller.light.LightStatusStateValue;
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
				result = LightStatusStateValue.ON.equals(stateValue) ? "on" : LightStatusStateValue.OFF.equals(stateValue) ? "off" : null;
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
				result = DPTXlatorBoolean.DPT_SWITCH.getLowerValue().equals(value) ? LightStatusStateValue.ON : DPTXlatorBoolean.DPT_SWITCH.getUpperValue().equals(value) ? LightStatusStateValue.OFF : null;
				break;
			}
			default: {
			}
		}
		return result;
	}
}
