package com.homesnap.engine.controller.light.legrand;

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
