package com.homesnap.engine.connector.openwebnet.light;

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

import com.homesnap.engine.controller.light.LightStatusStateValue;
import com.homesnap.engine.controller.light.legrand.LightLegrandStatusStateValue;
import com.homesnap.engine.controller.what.StateValue;


// LIGHT
public enum LightStatusConverter {

	// Basic LightStatus
	ON("1", LightStatusStateValue.ON),
	OFF("0", LightStatusStateValue.OFF),
	BLINKING("21", LightStatusStateValue.BLINKING),
	
	// LEgrand Light Status
	
	LIGHT_OFF("0", LightLegrandStatusStateValue.LIGHT_OFF), // TODO manage speed 0 to 255!
	LIGHT_ON("1", LightLegrandStatusStateValue.LIGHT_ON), // TODO manage speed!
	LIGHT_ON_20_PERCENT("2", LightLegrandStatusStateValue.LIGHT_ON_20_PERCENT),
	LIGHT_ON_30_PERCENT("3", LightLegrandStatusStateValue.LIGHT_ON_30_PERCENT),
	LIGHT_ON_40_PERCENT("4", LightLegrandStatusStateValue.LIGHT_ON_40_PERCENT),
	LIGHT_ON_50_PERCENT("5", LightLegrandStatusStateValue.LIGHT_ON_50_PERCENT),
	LIGHT_ON_60_PERCENT("6", LightLegrandStatusStateValue.LIGHT_ON_60_PERCENT),
	LIGHT_ON_70_PERCENT("7", LightLegrandStatusStateValue.LIGHT_ON_70_PERCENT),
	LIGHT_ON_80_PERCENT("8", LightLegrandStatusStateValue.LIGHT_ON_80_PERCENT),
	LIGHT_ON_90_PERCENT("9", LightLegrandStatusStateValue.LIGHT_ON_90_PERCENT),
	LIGHT_ON_100_PERCENT("10", LightLegrandStatusStateValue.LIGHT_ON_100_PERCENT),
	LIGHT_ON_DURING_1_MIN("11", LightLegrandStatusStateValue.LIGHT_ON_DURING_1_MIN),
	LIGHT_ON_DURING_2_MIN("12", LightLegrandStatusStateValue.LIGHT_ON_DURING_2_MIN),
	LIGHT_ON_DURING_3_MIN("13", LightLegrandStatusStateValue.LIGHT_ON_DURING_3_MIN),
	LIGHT_ON_DURING_4_MIN("14", LightLegrandStatusStateValue.LIGHT_ON_DURING_4_MIN),
	LIGHT_ON_DURING_5_MIN("15", LightLegrandStatusStateValue.LIGHT_ON_DURING_5_MIN),
	LIGHT_ON_DURING_15_MIN("16", LightLegrandStatusStateValue.LIGHT_ON_DURING_15_MIN),
	LIGHT_ON_DURING_30_SEC("17", LightLegrandStatusStateValue.LIGHT_ON_DURING_30_SEC),
	LIGHT_ON_DURING_HALF_SEC("18", LightLegrandStatusStateValue.LIGHT_ON_DURING_HALF_SEC),
	LIGHT_ERROR("19", LightLegrandStatusStateValue.LIGHT_ERROR),
	LIGHT_ON_BLINKING_HALF_SEC("20", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_HALF_SEC),
	LIGHT_ON_BLINKING_1_SEC("21", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_1_SEC),
	LIGHT_ON_BLINKING_1_AND_HALF_SEC("22", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_1_AND_HALF_SEC),
	LIGHT_ON_BLINKING_2_SEC("23", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_2_SEC),
	LIGHT_ON_BLINKING_2_AND_HALF_SEC("24", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_2_AND_HALF_SEC),
	LIGHT_ON_BLINKING_3_SEC("25", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_3_SEC),
	LIGHT_ON_BLINKING_3_AND_HALF_SEC("26", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_3_AND_HALF_SEC),
	LIGHT_ON_BLINKING_4_SEC("27", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_4_SEC),
	LIGHT_ON_BLINKING_4_AND_HALF_SEC("28", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_4_AND_HALF_SEC),
	LIGHT_ON_BLINKING_5_SEC("29", LightLegrandStatusStateValue.LIGHT_ON_BLINKING_5_SEC),
	LIGHT_ON_UP_ONE_LEVEL("30", LightLegrandStatusStateValue.LIGHT_ON_UP_ONE_LEVEL), // TODO manage speed!
	LIGHT_OFF_ONE_LEVEL("31", LightLegrandStatusStateValue.LIGHT_OFF_ONE_LEVEL), // TODO manage speed!

	LIGHT_FORCE_ON("1000#1", LightLegrandStatusStateValue.LIGHT_FORCE_ON),
	LIGHT_FORCE_OFF("1000#0", LightLegrandStatusStateValue.LIGHT_FORCE_OFF);


	
	private String code; // OpenWebNet code
	private StateValue value; // SnapHome code

	private LightStatusConverter(String code, LightLegrandStatusStateValue value) {
		this.code = code;
		this.value = value;
	}
	
	private LightStatusConverter(String code, LightStatusStateValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public StateValue getValue() {
		return value;
	}

	public static StateValue convert(String code) {
		for (LightStatusConverter light: LightStatusConverter.values()) {
			if (light.getCode().equals(code))
				return light.value;
		}
		return null;
	}

	public static String convert(StateValue name) {
		if (name == null) {
			return null;
		}
		for (LightStatusConverter light: LightStatusConverter.values()) {
			if (light.value.getValue().equals(name.getValue()))
				return light.code;
		}
		return null;
	}
}
