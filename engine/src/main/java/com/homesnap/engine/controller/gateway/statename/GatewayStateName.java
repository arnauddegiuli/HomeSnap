package com.homesnap.engine.controller.gateway.statename;

import com.homesnap.engine.controller.what.StateName;

public enum GatewayStateName implements StateName {
	DATE,
	DATETIME,
	DISTRIBUTION_VERSION,
	FIRMWARE_VERSION,
	IP_ADDRESS,
	KERNEL_VERSION,
	MAC_ADDRESS,
	MODEL,
	NETMASK,
	TIME,
	UPTIME;
	
	private GatewayStateName() {
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	public static GatewayStateName fromValue(String code) {
		for (GatewayStateName gd : GatewayStateName.values()) {
			if (gd.getName().equals(code))
				return gd;
		}
		return null;
	}
}
