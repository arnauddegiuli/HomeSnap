package com.homesnap.engine.controller.counter.stateName;

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

import com.homesnap.engine.controller.what.StateName;

public enum CounterStateName implements StateName {
	ACTIVE_POWER,
	TOTALE,
	PER_MONTH,
	CURRENT_MONTH,
	CURRENT_DAY,
	KERNEL_VERSION;
	
	private CounterStateName() {
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	public static CounterStateName fromValue(String code) {
		for (CounterStateName gd : CounterStateName.values()) {
			if (gd.getName().equals(code))
				return gd;
		}
		return null;
	}
}
