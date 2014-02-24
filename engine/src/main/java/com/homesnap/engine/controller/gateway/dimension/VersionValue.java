package com.homesnap.engine.controller.gateway.dimension;

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
	
	private int VERSION_POS = 0;
	private int RELEASE_POS = 1;
	private int BUILD_POS = 2;
	
	public VersionValue() {
	}
	
	public Version getVersion() {
		
//		int version = getIntValue(VERSION_POS);
//		int release = getIntValue(RELEASE_POS);
//		int build = getIntValue(BUILD_POS);
//		
//		Version ver = new Version();
//		ver.setVersion(version);
//		ver.setRelease(release);
//		ver.setBuild(build);
//		
//		return ver;
		return null;
	}
	
	public void setVersion(Version version) {
		
//		setIntValue(version.getVersion(), VERSION_POS, 2);
//		setIntValue(version.getRelease(), RELEASE_POS, 2);
//		setIntValue(version.getBuild(), BUILD_POS, 2);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
