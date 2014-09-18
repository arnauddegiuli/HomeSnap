package com.homesnap.engine.controller.heating.stateValue;

import com.homesnap.engine.controller.what.StateValue;

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

public class Offset implements StateValue{

	public enum Mode {
		ON, OFF, PROTECTION;
	}
	
	private Mode mode;
	private int degree;
	
	public Offset(Mode mode, int degree) {
		this.mode = mode;
		this.degree = degree;
	}

	public Mode getMode() {
		return mode;
	}
	
	public int getDegree() {
		return degree;
	}

	@Override
	public String getValue() {
		return mode.name() + " - " + getDegree();
	}
}
