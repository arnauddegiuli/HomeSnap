package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.gateway.Version;

public class KernelVersion extends DimensionStatusImpl {
	
	private int VERSION_POS = 0;
	private int RELEASE_POS = 0;
	private int BUILD_POS = 0;
	
	public KernelVersion() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Version
				new DimensionValueImpl(), // Release
				new DimensionValueImpl()  // Build
				},
			Gateway.GatewayDimension.KERNEL_VERSION.getCode()
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
}
