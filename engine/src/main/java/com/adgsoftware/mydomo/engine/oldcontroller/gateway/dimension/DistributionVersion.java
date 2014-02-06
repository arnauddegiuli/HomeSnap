package com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension;

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

import com.adgsoftware.mydomo.engine.oldcontroller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionValue;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.Version;

public class DistributionVersion extends DimensionStatusImpl {
	
	private int VERSION_POS = 0;
	private int RELEASE_POS = 1;
	private int BUILD_POS = 2;
	
	public DistributionVersion() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Version
				new DimensionValueImpl(), // Release
				new DimensionValueImpl()  // Build
				},
			Gateway.GatewayDimension.DISTRIBUTION_VERSION.getCode()
		);
	}
	
	public Version getVersion() {
		
		int version = getIntValue(VERSION_POS);
		int release = getIntValue(RELEASE_POS);
		int build = getIntValue(BUILD_POS);
		
		Version ver = new Version();
		ver.setVersion(version);
		ver.setRelease(release);
		ver.setBuild(build);
		
		return ver;
		
	}
	
	public void setVersion(Version version) {
		
		setIntValue(version.getVersion(), VERSION_POS, 2);
		setIntValue(version.getRelease(), RELEASE_POS, 2);
		setIntValue(version.getBuild(), BUILD_POS, 2);
	}
}
