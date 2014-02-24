package com.homesnap.engine.connector.openwebnet;

import com.homesnap.engine.controller.who.Who;

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

public class OpenWebNetWho {

	// WHO 28 €
	public final static String WHO_SCENARIO = "0";
	public final static String WHO_LIGHTING = "1";
	public final static String WHO_AUTOMATION = "2";
	public final static String WHO_POWER_MANAGEMENT = "3";
	public final static String WHO_HEATING_ADJUSTMENT = "4";
	public final static String WHO_MULTIMEDIA = "7";
	public final static String WHO_GATEWAY = "13";
	public final static String WHO_SOUND_SYSTEM = "16";
	public final static String WHO_DIAGNOSTIC_OF_HEATING_ADJUSTMENT = "1004";
	
	public static String convert(Who who) {
		switch (who) {
		case AUTOMATION:
			return WHO_AUTOMATION;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			return WHO_DIAGNOSTIC_OF_HEATING_ADJUSTMENT;
		case GATEWAY:
			return WHO_GATEWAY;
		case HEATING_ADJUSTMENT:
			return WHO_HEATING_ADJUSTMENT;
		case LIGHT:
			return WHO_LIGHTING;
		case POWER_MANAGEMENT:
			return WHO_POWER_MANAGEMENT;
		case SCENARIO:
			return WHO_SCENARIO;
		case SOUND_SYSTEM:
			return WHO_SOUND_SYSTEM;
		default:
			throw new UnsupportedOperationException(who.name() + " is not supported with OpenWebNet.");
		}
	}
	
	public static Who convert(String who) {
		if (WHO_SCENARIO.equals(who)) {
			return Who.SCENARIO;
		} else if (WHO_LIGHTING.equals(who)) {
			return Who.LIGHT;
		} else if (WHO_AUTOMATION.equals(who)) {
			return Who.AUTOMATION;
		} else if (WHO_POWER_MANAGEMENT.equals(who)) {
			return Who.POWER_MANAGEMENT;
		} else if (WHO_HEATING_ADJUSTMENT.equals(who)) {
			return Who.HEATING_ADJUSTMENT;
		} else if (WHO_MULTIMEDIA.equals(who)) {
			return Who.MULTIMEDIA;
		} else if (WHO_GATEWAY.equals(who)) {
			return Who.GATEWAY;
		} else if (WHO_SOUND_SYSTEM.equals(who)) {
			return Who.SOUND_SYSTEM;
		} else if (WHO_DIAGNOSTIC_OF_HEATING_ADJUSTMENT.equals(who)) {
			return Who.DIAGNOSTIC_OF_HEATING_ADJUSTMENT;
		} else {
			throw new UnsupportedOperationException(who + " is not supported with OpenWebNet.");
		}
	}
}
