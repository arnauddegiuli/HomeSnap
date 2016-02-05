package com.homesnap.engine.connector.openwebnet.light;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.homesnap.engine.controller.light.LightStateName;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
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

import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.what.impl.IntegerState;
import com.homesnap.engine.controller.what.impl.OnOffState;
import com.homesnap.engine.controller.what.impl.PercentageState;


// LIGHT
public enum LightStatusConverter {
	
	// LEgrand Light Status
	LIGHT_OFF("0"), // TODO manage speed 0 to 255!
	LIGHT_ON("1"), // TODO manage speed!
	LIGHT_ON_20_PERCENT("2"),
	LIGHT_ON_30_PERCENT("3"),
	LIGHT_ON_40_PERCENT("4"),
	LIGHT_ON_50_PERCENT("5"),
	LIGHT_ON_60_PERCENT("6"),
	LIGHT_ON_70_PERCENT("7"),
	LIGHT_ON_80_PERCENT("8"),
	LIGHT_ON_90_PERCENT("9"),
	LIGHT_ON_100_PERCENT("10"),
	LIGHT_ON_DURING_1_MIN("11"),
	LIGHT_ON_DURING_2_MIN("12"),
	LIGHT_ON_DURING_3_MIN("13"),
	LIGHT_ON_DURING_4_MIN("14"),
	LIGHT_ON_DURING_5_MIN("15"),
	LIGHT_ON_DURING_15_MIN("16"),
	LIGHT_ON_DURING_30_SEC("17"),
	LIGHT_ON_DURING_HALF_SEC("18"),
	LIGHT_ERROR("19"),
	LIGHT_ON_BLINKING_HALF_SEC("20"),
	LIGHT_ON_BLINKING_1_SEC("21"),
	LIGHT_ON_BLINKING_1_AND_HALF_SEC("22"),
	LIGHT_ON_BLINKING_2_SEC("23"),
	LIGHT_ON_BLINKING_2_AND_HALF_SEC("24"),
	LIGHT_ON_BLINKING_3_SEC("25"),
	LIGHT_ON_BLINKING_3_AND_HALF_SEC("26"),
	LIGHT_ON_BLINKING_4_SEC("27"),
	LIGHT_ON_BLINKING_4_AND_HALF_SEC("28"),
	LIGHT_ON_BLINKING_5_SEC("29"),
	LIGHT_ON_UP_ONE_LEVEL("30"), // TODO manage speed!
	LIGHT_OFF_ONE_LEVEL("31"), // TODO manage speed!

	LIGHT_FORCE_ON("1000#1"),
	LIGHT_FORCE_OFF("1000#0");

	private String value;
	
	private LightStatusConverter(String value) {
		this.value = value;
	}
	
	private static LightStatusConverter fromValue(String value) {
		for (LightStatusConverter c : LightStatusConverter.values()) {
			if (c.getCode().equals(value)) {
				return c;
			}
		}
		return null;
	}

	public String getCode() {
		return value;
	}
	
	public static List<What> convert(String code, Map<String, State<?>> controllerStateList) {
		
		LightStatusConverter c = LightStatusConverter.fromValue(code);
		List<What> result = new ArrayList<What>();
		switch (c) {
		case LIGHT_ERROR:
			break;
		case LIGHT_FORCE_OFF:
			result.add(new What(LightStateName.status.name(), OnOffState.Off));
			break;
		case LIGHT_FORCE_ON:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_OFF:
			result.add(new What(LightStateName.status.name(), OnOffState.Off));
			break;
		case LIGHT_OFF_ONE_LEVEL:
			break;
		case LIGHT_ON:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_100_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(100)));
			break;
		case LIGHT_ON_20_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(20)));
			break;
		case LIGHT_ON_30_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(30)));
			break;
		case LIGHT_ON_40_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(40)));
			break;
		case LIGHT_ON_50_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(50)));
			break;
		case LIGHT_ON_60_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(60)));
			break;
		case LIGHT_ON_70_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(70)));
			break;
		case LIGHT_ON_80_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(80)));
			break;
		case LIGHT_ON_90_PERCENT:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.level.name(), new IntegerState(90)));
			break;
		case LIGHT_ON_BLINKING_1_AND_HALF_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(1500)));
			break;
		case LIGHT_ON_BLINKING_1_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(1000)));
			break;
		case LIGHT_ON_BLINKING_2_AND_HALF_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(2500)));
			break;
		case LIGHT_ON_BLINKING_2_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(2000)));
			break;
		case LIGHT_ON_BLINKING_3_AND_HALF_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(3500)));
			break;
		case LIGHT_ON_BLINKING_3_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(3000)));
			break;
		case LIGHT_ON_BLINKING_4_AND_HALF_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(4500)));
			break;
		case LIGHT_ON_BLINKING_4_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(4000)));
			break;
		case LIGHT_ON_BLINKING_5_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(5000)));
			break;
		case LIGHT_ON_BLINKING_HALF_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			result.add(new What(LightStateName.blink.name(), OnOffState.On));
			result.add(new What(LightStateName.blink_time.name(), new IntegerState(500)));
			break;
		case LIGHT_ON_DURING_15_MIN:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_1_MIN:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_2_MIN:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_30_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_3_MIN:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_4_MIN:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_5_MIN:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_DURING_HALF_SEC:
			result.add(new What(LightStateName.status.name(), OnOffState.On));
			break;
		case LIGHT_ON_UP_ONE_LEVEL:
			break;
		default:
			break;
		}
		
		return result;
	}

	public static List<String> convert(What what, Map<String, State<?>> controllerStateList) {
		if (what == null) {
			return null;
		}
		
		List<String> result = new ArrayList<String>();
		LightStateName s = LightStateName.valueOf(what.getName());
		
		OnOffState status = (OnOffState) controllerStateList.get(LightStateName.status.name());
		PercentageState level = (PercentageState) controllerStateList.get(LightStateName.level.name());
		OnOffState blink = (OnOffState) controllerStateList.get(LightStateName.blink.name());
		IntegerState blink_time = (IntegerState) controllerStateList.get(LightStateName.blink_time.name());
		switch (s) {
		case color:
			return result;
		case status:
			status = (OnOffState) what.getValue();
			if (OnOffState.Off == status) {
				result.add(LightStatusConverter.LIGHT_OFF.value);
				return result;
			} else {
				result.add(LightStatusConverter.LIGHT_ON.value);
				return result;
			}
		case level:
			level = (PercentageState) what.getValue();
			break;
		case blink:
			blink = (OnOffState) what.getValue();
			break;
		case blink_time:
			blink_time = (IntegerState) what.getValue();
			break;
			
			 
		}
		
//		if (OnOffState.On == blink) {
//			result.add(getLevel(level));
//		} else if () {
//			
//		} else {
//			result.add(LightStatusConverter.LIGHT_ON.value);
//		}
//		
//		
//		(OnOffState) controllerStateList.get(LightStateName.blink.name());
//
//		OnOffState status =  (OnOffState) controllerStateList.get(LightStateName.status.name());
//		if (OnOffState.On == status) {
//			result.add(getBlinkTime(value));
//		}
		
		return result;
	}
	
	private static String getBlinkTime(IntegerState value) {
		int time = value.getIntValue();
		
		if (time <501) {
			return LIGHT_ON_BLINKING_HALF_SEC.name();
		} else if (time < 1001) {
			return LIGHT_ON_BLINKING_1_SEC.name();
		} else if (time < 1501) {
			return LIGHT_ON_BLINKING_1_AND_HALF_SEC.name();
		} else if (time < 2001) {
			return LIGHT_ON_BLINKING_2_SEC.name();
		} else if (time < 2501) {
			return LIGHT_ON_BLINKING_2_AND_HALF_SEC.name();
		} else if (time < 3001) {
			return LIGHT_ON_BLINKING_3_SEC.name();
		} else if (time < 3501) {
			return LIGHT_ON_BLINKING_3_AND_HALF_SEC.name();
		} else if (time < 4001) {
			return LIGHT_ON_BLINKING_4_SEC.name();
		} else if (time < 4501) {
			return LIGHT_ON_BLINKING_4_AND_HALF_SEC.name();
		} else {
			return LIGHT_ON_BLINKING_5_SEC.name();
		} 
	
	}
	
	private static String getLevel(PercentageState value) {
		int i = value.getValue();
		if (i < 20) {
			return LightStatusConverter.LIGHT_OFF.name();
		} else if (i < 30) {
			return LightStatusConverter.LIGHT_ON_20_PERCENT.name();
		} else if (i < 40) {
			return LightStatusConverter.LIGHT_ON_30_PERCENT.name();
		} else if (i < 50) {
			return LightStatusConverter.LIGHT_ON_40_PERCENT.name();
		} else if (i < 60) {
			return LightStatusConverter.LIGHT_ON_50_PERCENT.name();
		} else if (i < 70) {
			return LightStatusConverter.LIGHT_ON_60_PERCENT.name();
		} else if (i < 80) {
			return LightStatusConverter.LIGHT_ON_70_PERCENT.name();
		} else if (i < 90) {
			return LightStatusConverter.LIGHT_ON_80_PERCENT.name();
		} else if (i < 100) {
			return LightStatusConverter.LIGHT_ON_90_PERCENT.name();
		} else {
			return LightStatusConverter.LIGHT_ON.name();
		}
	}
}
