package com.homesnap.engine.controller.light;

import com.homesnap.engine.controller.what.StateName;

/**
 * Enumeration of state names for a standard HomeSnap light controller.
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public enum LightStateName implements StateName {
	
	/** Contains the current status of the light (ie: on, off, blinking ...) depending on vendor capabilities. */
	STATUS,
	
	/** Contains the current level of light for dimmable devices. */
	LEVEL,
	
	/** Contains the hexadecimal color code for RGB devices. */
	COLOR;
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
}
