package com.homesnap.engine.controller.light;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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

import java.util.ArrayList;
import java.util.List;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.impl.IntegerState;
import com.homesnap.engine.controller.what.impl.OnOffState;
import com.homesnap.engine.controller.what.impl.PercentageState;
import com.homesnap.engine.controller.what.impl.RGBState;
import com.homesnap.engine.controller.who.Who;

public class Light extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Who getWho() {
		return Who.LIGHT;
	}

	public OnOffState getStatus() {
		return (OnOffState) get(LightStateName.status.name().toLowerCase());
	}

	public void setStatus(OnOffState status) {
		set(LightStateName.status.name().toLowerCase(), status);
	}

	public PercentageState getLevel() {
		return (PercentageState) get(LightStateName.level.name());
	}
	
	public void setLevel(PercentageState value) {
		set(LightStateName.level.name(), value);
	}
	
	public OnOffState getBlink() {
		return (OnOffState) get(LightStateName.blink_time.name());
	}
	
	public void setBlink(OnOffState value) {
		set(LightStateName.blink_time.name(), value);
	}
	
	public IntegerState getBlinkTime() {
		// in millisecond
		return (IntegerState) get(LightStateName.blink.name());
	}
	
	public void setBlinkTime(IntegerState value) {
		// in millisecond
		set(LightStateName.blink.name(), value);
	}
	
	public RGBState getColor() {
		return (RGBState) get(LightStateName.color.name());
	}

	@Override
	public List<String> getStateList() {
		List<String> result = new ArrayList<String>();
		LightStateName[] list = LightStateName.values();
		for (int i = 0; i < list.length; i++) {
			result.add(list[i].name());
		};
		return result;
	}
}
