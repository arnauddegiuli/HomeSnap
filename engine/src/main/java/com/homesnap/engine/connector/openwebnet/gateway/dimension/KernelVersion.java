package com.homesnap.engine.connector.openwebnet.gateway.dimension;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
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


public class KernelVersion extends DimensionStatusImpl {
	
	private int VERSION_POS = 0;
	private int RELEASE_POS = 1;
	private int BUILD_POS = 2;
	
	public KernelVersion() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Version
				new DimensionValueImpl(), // Release
				new DimensionValueImpl()  // Build
				},
			GatewayDimension.KERNEL_VERSION.getCode()
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

	@Override
	public StateValue getStateValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValueList(StateValue value) {
		// TODO Auto-generated method stub
		
	}
}
