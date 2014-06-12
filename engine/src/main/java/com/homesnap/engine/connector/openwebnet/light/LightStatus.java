package com.homesnap.engine.connector.openwebnet.light;

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

import com.homesnap.engine.controller.light.stateValue.LightStatusValue;
import com.homesnap.engine.controller.what.StateValue;


// LIGHT
public enum LightStatus {
	LIGHT_OFF("0", LightStatusValue.LIGHT_OFF), // TODO manage speed 0 to 255!
	LIGHT_ON("1", LightStatusValue.LIGHT_ON), // TODO manage speed!
	LIGHT_ON_20_PERCENT("2", LightStatusValue.LIGHT_ON_20_PERCENT),
	LIGHT_ON_30_PERCENT("3", LightStatusValue.LIGHT_ON_30_PERCENT),
	LIGHT_ON_40_PERCENT("4", LightStatusValue.LIGHT_ON_40_PERCENT),
	LIGHT_ON_50_PERCENT("5", LightStatusValue.LIGHT_ON_50_PERCENT),
	LIGHT_ON_60_PERCENT("6", LightStatusValue.LIGHT_ON_60_PERCENT),
	LIGHT_ON_70_PERCENT("7", LightStatusValue.LIGHT_ON_70_PERCENT),
	LIGHT_ON_80_PERCENT("8", LightStatusValue.LIGHT_ON_80_PERCENT),
	LIGHT_ON_90_PERCENT("9", LightStatusValue.LIGHT_ON_90_PERCENT),
	LIGHT_ON_100_PERCENT("10", LightStatusValue.LIGHT_ON_100_PERCENT),
	LIGHT_ON_DURING_1_MIN("11", LightStatusValue.LIGHT_ON_DURING_1_MIN),
	LIGHT_ON_DURING_2_MIN("12", LightStatusValue.LIGHT_ON_DURING_2_MIN),
	LIGHT_ON_DURING_3_MIN("13", LightStatusValue.LIGHT_ON_DURING_3_MIN),
	LIGHT_ON_DURING_4_MIN("14", LightStatusValue.LIGHT_ON_DURING_4_MIN),
	LIGHT_ON_DURING_5_MIN("15", LightStatusValue.LIGHT_ON_DURING_5_MIN),
	LIGHT_ON_DURING_15_MIN("16", LightStatusValue.LIGHT_ON_DURING_15_MIN),
	LIGHT_ON_DURING_30_SEC("17", LightStatusValue.LIGHT_ON_DURING_30_SEC),
	LIGHT_ON_DURING_HALF_SEC("18", LightStatusValue.LIGHT_ON_DURING_HALF_SEC),
	LIGHT_ERROR("19", LightStatusValue.LIGHT_ERROR),
	LIGHT_ON_BLINKING_HALF_SEC("20", LightStatusValue.LIGHT_ON_BLINKING_HALF_SEC),
	LIGHT_ON_BLINKING_1_SEC("21", LightStatusValue.LIGHT_ON_BLINKING_1_SEC),
	LIGHT_ON_BLINKING_1_AND_HALF_SEC("22", LightStatusValue.LIGHT_ON_BLINKING_1_AND_HALF_SEC),
	LIGHT_ON_BLINKING_2_SEC("23", LightStatusValue.LIGHT_ON_BLINKING_2_SEC),
	LIGHT_ON_BLINKING_2_AND_HALF_SEC("24", LightStatusValue.LIGHT_ON_BLINKING_2_AND_HALF_SEC),
	LIGHT_ON_BLINKING_3_SEC("25", LightStatusValue.LIGHT_ON_BLINKING_3_SEC),
	LIGHT_ON_BLINKING_3_AND_HALF_SEC("26", LightStatusValue.LIGHT_ON_BLINKING_3_AND_HALF_SEC),
	LIGHT_ON_BLINKING_4_SEC("27", LightStatusValue.LIGHT_ON_BLINKING_4_SEC),
	LIGHT_ON_BLINKING_4_AND_HALF_SEC("28", LightStatusValue.LIGHT_ON_BLINKING_4_AND_HALF_SEC),
	LIGHT_ON_BLINKING_5_SEC("29", LightStatusValue.LIGHT_ON_BLINKING_5_SEC),
	LIGHT_ON_UP_ONE_LEVEL("30", LightStatusValue.LIGHT_ON_UP_ONE_LEVEL), // TODO manage speed!
	LIGHT_OFF_ONE_LEVEL("31", LightStatusValue.LIGHT_OFF_ONE_LEVEL), // TODO manage speed!

	LIGHT_FORCE_ON("1000#1", LightStatusValue.LIGHT_FORCE_ON),
	LIGHT_FORCE_OFF("1000#0", LightStatusValue.LIGHT_FORCE_OFF);

	private String code; // OpenWebNet code
	private StateValue value; // SnapHome code

	private LightStatus(String code, StateValue value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public StateValue getValue() {
		return value;
	}

	public static LightStatus fromValue(String code) {
		for (LightStatus light: LightStatus.values()) {
			if (light.getCode().equals(code))
				return light;
		}
		return null;
	}

	public static LightStatus fromValue(StateValue name) {
		if (name == null) {
			return null;
		}
		for (LightStatus light: LightStatus.values()) {
			if (light.value.getValue().equals(name.getValue()))
				return light;
		}
		return null;
	}
}
