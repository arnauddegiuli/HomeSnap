package com.adgsoftware.mydomo.engine.controller.light;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Light extends Controller<Light.LightStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// LIGHT
	public enum LightStatus implements Status {
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
		LIGHT_OFF_ONE_LEVEL("31"); // TODO manage speed!
		
		private String code = "";
		private LightStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
		
	public Light() {
	}
	
	@Override
	public String getWho() {
		return Command.WHO_LIGHTING;
	}

	@Override
	public LightStatus getStatus(String code) {
		if (code == null) 
			return null;
		for (LightStatus status : LightStatus.values()) {
			if (code.equals(status.getCode())) {
				return status;
			}
		}	
		return null;
	}
}