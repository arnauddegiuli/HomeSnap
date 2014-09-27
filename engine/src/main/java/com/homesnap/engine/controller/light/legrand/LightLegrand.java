package com.homesnap.engine.controller.light.legrand;

import com.homesnap.engine.controller.light.Light;
import com.homesnap.engine.controller.light.LightStateName;
import com.homesnap.engine.controller.light.LightStatusStateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class LightLegrand extends Light {
	
	/** */
	private static final long serialVersionUID = 751528707497249180L;

	public static void main(String[] args) {
		
		Light l = new Light();
		l.setStatus(LightStatusStateValue.ON);
		try {
			l.set(LightStateName.STATUS.name(), "LIGHT_ON");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LightLegrand lg = new LightLegrand();
		lg.setStatus(LightLegrandStatusStateValue.LIGHT_ON);
		lg.setStatus(LightLegrandStatusStateValue.LIGHT_ON_50_PERCENT);
	}
}
