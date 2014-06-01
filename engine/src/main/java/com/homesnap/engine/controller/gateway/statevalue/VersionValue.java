package com.homesnap.engine.controller.gateway.statevalue;

import com.homesnap.engine.controller.gateway.Version;
import com.homesnap.engine.controller.what.StateValue;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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


public class VersionValue implements StateValue {

	private Version version;
	
	public VersionValue() {
	}
	
	public Version getVersion() {
		return version;
	}
	
	public void setVersion(Version version) {
		this.version = version;
	}

	@Override
	public String getValue() {
		return "".concat(String.valueOf(version.getVersion()))
				.concat(".").concat(String.valueOf(version.getRelease()))
				.concat(".").concat(String.valueOf(version.getBuild()));
	}
}
