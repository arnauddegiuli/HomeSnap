package com.homesnap.engine.controller.light;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class LightLegrand extends Light {
	
	public static void main(String[] args) {
		
		Light l = new Light();
		l.setStatus("On");
		try {
			l.setStatus("LIGHT_ON");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LightLegrand lg = new LightLegrand();
		lg.setStatus("LIGHT_ON");
		lg.setStatus("LIGHT_ON_50_PERCENT");
	}
}
