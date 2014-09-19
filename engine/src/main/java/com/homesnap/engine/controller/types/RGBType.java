package com.homesnap.engine.controller.types;

import java.util.regex.Pattern;

import com.homesnap.engine.controller.what.StateValueType;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class RGBType extends StateValueType {
	
	/** */
	private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
	
	/** */
	private int[] color = new int[] {0, 0, 0};

	@Override
	public String getValue() {
		return String.format("#%02x%02x%02x", color[0], color[1], color[2]);
	}

	@Override
	protected void setValue(String value) {
		if (value.length() > 0) {
			String hexCode = value;
			if (value.charAt(0) == '#') {
				hexCode = value.substring(1);
			}
			if (HEX_COLOR_PATTERN.matcher(hexCode).matches()) {
				int rgb = Integer.parseInt(hexCode, 16);
			    color[0] = (rgb & 0xFF0000) >> 16;
			    color[1] = (rgb & 0xFF00) >> 8;
			    color[2] = (rgb & 0xFF);
			    return;
			}
		}
		throw new IllegalArgumentException("Value "+ value +" is not a valid hexadecimal color code.");
	}
	
	@Override
	public String toString() {
		return getValue();
	}
}
