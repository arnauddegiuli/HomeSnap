package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.gateway.Version;

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
