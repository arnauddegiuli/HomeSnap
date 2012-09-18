package com.adgsoftware.mydomo.test;

import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;

public interface Constant {

	public interface Light1 {
		public static String LIGHT_ADDRESS = "12";
		public static LightStatus LIGHT_ON = Light.LightStatus.LIGHT_ON;
		public static LightStatus LIGHT_OFF = Light.LightStatus.LIGHT_OFF;
		public static String LIGHT_WHERE = "12";
	}
	
	public interface Gateway {
		public static String GATEWAY_ADDRESS = "22";
	}
}
