package com.homesnap.engine.controller.light;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.state.StateName;
import com.homesnap.engine.controller.state.type.DoubleValue;

/**
 * 
 * @author DRIESBACH Olivier
 */
public class LightController extends Controller {
	
	/**
	 * 
	 */
	public LightController(String address) {
		super(address);
	}
	
	/**
	 * Set the 
	 * @param value
	 */
	public void setLevel(double value) {
		if ((value < 0) || (value > 100)) {
			throw new IllegalArgumentException("The level value must be between 0 and 100.");
		}
		set(StateName.LEVEL, new DoubleValue(value));
	}

	@Override
	protected void initStateTypes() {
		declareState(StateName.STATUS, LightStatus.class);
		declareState(StateName.LEVEL, DoubleValue.class);
	}
}
