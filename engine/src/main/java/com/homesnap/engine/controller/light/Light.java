package com.homesnap.engine.controller.light;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.types.RGBType;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class Light extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public Light() {
	}

	@Override
	public Who getWho() {
		return Who.LIGHT;
	}

	/**
	 * 
	 * @return
	 */
	public StateValue getStatus() {
		return get(LightStateName.STATUS);
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(StateValue status) {
		set(LightStateName.STATUS, status);
	}
	
	/**
	 * 
	 * @return
	 */
	public StateValue getLevel() {
		return get(LightStateName.LEVEL);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setLevel(StateValue value) {
		set(LightStateName.LEVEL, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public StateValue getBlinkTime() {
		return get(LightStateName.BLINK_TIME);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setBlinkTime(StateValue value) {
		set(LightStateName.BLINK_TIME, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public RGBType getColor() {
		return (RGBType) get(LightStateName.COLOR);
	}
}
