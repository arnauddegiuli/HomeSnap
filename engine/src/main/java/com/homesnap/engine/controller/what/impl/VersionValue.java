package com.homesnap.engine.controller.what.impl;

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

import com.homesnap.engine.controller.what.StateValue;

public class VersionValue implements StateValue {

	private int version, release, build;
	
	public VersionValue (int version, int release, int build) {
		this.version = version;
		this.release = release;
		this.build = build;
	}
	
	public int getVersion() {
		return version;
	}

	public int getRelease() {
		return release;
	}

	public int getBuild() {
		return build;
	}

	@Override
	public String getValue() {
		return "".concat(String.valueOf(version))
				.concat(".").concat(String.valueOf(release))
				.concat(".").concat(String.valueOf(build));
	}

}
