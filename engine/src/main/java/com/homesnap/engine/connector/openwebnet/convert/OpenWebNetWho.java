package com.homesnap.engine.connector.openwebnet.convert;

import com.homesnap.engine.controller.who.Who;

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

public enum OpenWebNetWho {

	// WHO 28 €
	WHO_SCENARIO("0"),
	WHO_LIGHTING("1"),
	WHO_AUTOMATION("2"),
	WHO_POWER_MANAGEMENT("3"),
	WHO_HEATING_ADJUSTMENT("4"),
	WHO_BURGLAR_ALARM("5"), // not supported
	WHO_DOOR_ENTRY("6"), // not supported
	WHO_MULTIMEDIA("7"),
	WHO_AUXILIARY("9"), // not supported
	WHO_GATEWAY("13"),
	WHO_LIGHT_SHUTTERS_ACTUATORS_LOCK("14"), // not supported
	WHO_CEN_SCENARIO_SCHEDULER_SWITCH("15"), // not supported
	WHO_SOUND_SYSTEM("16"),
	WHO_SCENARIO_PROGRAMMING("17"), // not supported
	WHO_ENERGY_MANAGEMENT("18"),
	WHO_LIGHT_MANAGEMENT("24"), // not supported
	WHO_CEN_SCENARIO_SCHEDULER_BUTTONS("25"), // not supported
	WHO_DIAGNOSTIC("1000"), // not supported
	WHO_AUTOMATION_DIAGNOSTIC("1001"), // not supported
	WHO_DIAGNOSTIC_OF_HEATING_ADJUSTMENT("1004"),
	WHO_DEVICE_DIAGNOSTIC("1013"); // not supported

	private String code;

	private OpenWebNetWho(String code) {
		this.code = code;
	}

	public String getValue() {
		return code;
	}
	
	public static OpenWebNetWho convert(Who who) {
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
		case ENERGY_MANAGEMENT:
			return WHO_ENERGY_MANAGEMENT;
		default:
			throw new UnsupportedOperationException(who.name() + " is not supported with OpenWebNet.");
		}
	}
	
	public static Who convert(String w) {

		OpenWebNetWho who = null;
		for (OpenWebNetWho ow : OpenWebNetWho.values()) {
			if (ow.getValue().equals(w)) {
				who = ow;
				break;
			}
		}
		
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
		} else if (WHO_ENERGY_MANAGEMENT.equals(who)) { 
			return Who.ENERGY_MANAGEMENT;
		} else if (WHO_DIAGNOSTIC_OF_HEATING_ADJUSTMENT.equals(who)) {
			return Who.DIAGNOSTIC_OF_HEATING_ADJUSTMENT;
		} else {
			throw new UnsupportedOperationException(w + " is not supported with OpenWebNet.");
		}
	}
}
