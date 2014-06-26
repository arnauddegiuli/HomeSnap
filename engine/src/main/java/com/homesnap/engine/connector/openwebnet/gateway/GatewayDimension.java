package com.homesnap.engine.connector.openwebnet.gateway;

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

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.Date;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.DateTime;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.DistributionVersion;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.FirmwareVersion;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.IpAddress;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.KernelVersion;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.MacAddress;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.Model;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.NetMask;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.Time;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.UpTime;
import com.homesnap.engine.controller.gateway.stateName.GatewayStateName;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;

public enum GatewayDimension {
	DATE("1", GatewayStateName.DATE, Date.class),
	DATETIME("22", GatewayStateName.DATETIME, DateTime.class),
	DISTRIBUTION_VERSION("24", GatewayStateName.DISTRIBUTION_VERSION, DistributionVersion.class),
	FIRMWARE_VERSION("16", GatewayStateName.FIRMWARE_VERSION, FirmwareVersion.class),
	IP_ADDRESS("10", GatewayStateName.IP_ADDRESS, IpAddress.class),
	KERNEL_VERSION("23", GatewayStateName.KERNEL_VERSION, KernelVersion.class),
	MAC_ADDRESS("12", GatewayStateName.MAC_ADDRESS, MacAddress.class),
	MODEL("15", GatewayStateName.MODEL, Model.class),
	NETMASK("11", GatewayStateName.NETMASK, NetMask.class),
	TIME("0", GatewayStateName.TIME, Time.class),
	UPTIME("19", GatewayStateName.UPTIME, UpTime.class);
	
	private String openWebNetCode;
	private StateName name;
	private Class<? extends DimensionStatus<? extends StateValue>> clazz;

	private GatewayDimension(String code, StateName name, Class<? extends DimensionStatus<? extends StateValue>> clazz) {
		this.openWebNetCode = code;
		this.name = name;
		this.clazz = clazz;
	}
	
	public DimensionStatus<? extends StateValue> createDimensionStatus() {
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
