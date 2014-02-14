package com.adgsoftware.mydomo.engine.connector.openwebnet.gateway;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionStatus;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.Date;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.DateTime;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.DistributionVersion;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.FirmwareVersion;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.IpAddress;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.KernelVersion;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.MacAddress;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.Model;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.NetMask;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.Time;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.dimension.UpTime;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.what.StateName;

public enum GatewayDimension {
	DATE("1", Gateway.GatewayStateName.DATE, Date.class),
	DATETIME("22", Gateway.GatewayStateName.DATETIME, DateTime.class),
	DISTRIBUTION_VERSION("24", Gateway.GatewayStateName.DISTRIBUTION_VERSION, DistributionVersion.class),
	FIRMWARE_VERSION("16", Gateway.GatewayStateName.FIRMWARE_VERSION, FirmwareVersion.class),
	IP_ADDRESS("10", Gateway.GatewayStateName.IP_ADDRESS, IpAddress.class),
	KERNEL_VERSION("23", Gateway.GatewayStateName.KERNEL_VERSION, KernelVersion.class),
	MAC_ADDRESS("12", Gateway.GatewayStateName.MAC_ADDRESS, MacAddress.class),
	MODEL("15", Gateway.GatewayStateName.MODEL, Model.class),
	NETMASK("11", Gateway.GatewayStateName.NETMASK, NetMask.class),
	TIME("0", Gateway.GatewayStateName.TIME, Time.class),
	UPTIME("19", Gateway.GatewayStateName.UPTIME, UpTime.class);
	
	private String openWebNetCode;
	private StateName name;
	private Class<? extends DimensionStatus> clazz;
	private GatewayDimension(String code, StateName name, Class<? extends DimensionStatus> clazz) {
		this.openWebNetCode = code;
		this.name = name;
		this.clazz = clazz;
	}
	
	public DimensionStatus createDimensionStatus() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public String getCode() {
		return openWebNetCode;
	}

	public StateName getName() {
		return name;
	}

	public static GatewayDimension fromValue(String code) {
		for (GatewayDimension gd : GatewayDimension.values()) {
			if (gd.getCode().equals(code))
				return gd;
		}
		
		return null;
	}

	public static GatewayDimension fromValue(StateName name) {
		if (name == null) {
			return null;
		}
		for (GatewayDimension gd : GatewayDimension.values()) {
			if (gd.getName().getName().equals(name.getName()))
				return gd;
		}
		return null;
	}
}
