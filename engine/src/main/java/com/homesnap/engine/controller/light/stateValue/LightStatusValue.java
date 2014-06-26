package com.homesnap.engine.controller.light.stateValue;

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

import com.homesnap.engine.controller.what.StateValue;

public enum LightStatusValue implements StateValue {
	LIGHT_OFF,
	LIGHT_ON,
	LIGHT_ON_20_PERCENT,
	LIGHT_ON_30_PERCENT,
	LIGHT_ON_40_PERCENT,
	LIGHT_ON_50_PERCENT,
	LIGHT_ON_60_PERCENT,
	LIGHT_ON_70_PERCENT,
	LIGHT_ON_80_PERCENT,
	LIGHT_ON_90_PERCENT,
	LIGHT_ON_100_PERCENT,
	LIGHT_ON_DURING_1_MIN,
	LIGHT_ON_DURING_2_MIN,
	LIGHT_ON_DURING_3_MIN,
	LIGHT_ON_DURING_4_MIN,
	LIGHT_ON_DURING_5_MIN,
	LIGHT_ON_DURING_15_MIN,
	LIGHT_ON_DURING_30_SEC,
	LIGHT_ON_DURING_HALF_SEC,
	LIGHT_ERROR,
	LIGHT_ON_BLINKING_HALF_SEC,
	LIGHT_ON_BLINKING_1_SEC,
	LIGHT_ON_BLINKING_1_AND_HALF_SEC,
	LIGHT_ON_BLINKING_2_SEC,
	LIGHT_ON_BLINKING_2_AND_HALF_SEC,
	LIGHT_ON_BLINKING_3_SEC,
	LIGHT_ON_BLINKING_3_AND_HALF_SEC,
	LIGHT_ON_BLINKING_4_SEC,
	LIGHT_ON_BLINKING_4_AND_HALF_SEC,
	LIGHT_ON_BLINKING_5_SEC,
	LIGHT_ON_UP_ONE_LEVEL, // TODO manage speed!
	LIGHT_OFF_ONE_LEVEL, // TODO manage speed!

	LIGHT_FORCE_ON,
	LIGHT_FORCE_OFF;

	@Override
	public String getValue() {
		return name();
	}
}