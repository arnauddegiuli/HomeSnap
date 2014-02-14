package com.homesnap.engine.controller.light;

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


import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.state.StateName;
import com.homesnap.engine.controller.state.type.DoubleValue;

/**
 * 
 * @author DRIESBACH Olivier
 */
public class LightController extends Controller {
	
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public LightController(String address) {
		super(address);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStatus() {
		return get(StateName.STATUS).getValue();
	}
	
	/**
	 * 
	 * @param status
	 */
	public void setStatus(LightStatus status) {
		set(StateName.STATUS, status);
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
