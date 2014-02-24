package com.homesnap.engine.connector.openwebnet;

/*
 * #%L
 * MyDomoEngine
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

import com.homesnap.engine.connector.openwebnet.light.LightStatus;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.who.Who;

public class StatusMapping {

	public static String convert(Who who, StateValue stateValue) {
		switch (who) {
		case LIGHT:
			return LightStatus.fromValue(stateValue).getCode(); // TODO manage null
		default:
			return stateValue.getValue(); // TODO mapping
		}
			
		
	}
	
	public static StateValue convert(Who who, String code) {
		switch (who) {
		case LIGHT:
			return LightStatus.fromValue(code).getValue(); // TODO manage null
		default:
			return null; // TODO mapping
		}
		
	}
}
