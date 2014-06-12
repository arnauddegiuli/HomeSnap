package com.homesnap.engine.controller.gateway;

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


import java.util.Date;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.gateway.statename.GatewayStateName;
import com.homesnap.engine.controller.what.impl.DateValue;
import com.homesnap.engine.controller.what.impl.IpAddressValue;
import com.homesnap.engine.controller.what.impl.StringValue;
import com.homesnap.engine.controller.what.impl.VersionValue;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;


public class Gateway extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;

	public Gateway() {
	}

	@Override
	protected void initStateTypes() {
		declareState(GatewayStateName.DATE, DateValue.class);
		declareState(GatewayStateName.DATETIME, DateValue.class);
		declareState(GatewayStateName.DISTRIBUTION_VERSION, VersionValue.class);
		declareState(GatewayStateName.FIRMWARE_VERSION, VersionValue.class);
		declareState(GatewayStateName.IP_ADDRESS, IpAddressValue.class);
		declareState(GatewayStateName.KERNEL_VERSION, VersionValue.class);
		declareState(GatewayStateName.MAC_ADDRESS, IpAddressValue.class);
		declareState(GatewayStateName.MODEL, StringValue.class);
		declareState(GatewayStateName.NETMASK, IpAddressValue.class);
		declareState(GatewayStateName.TIME, DateValue.class);
		declareState(GatewayStateName.UPTIME, DateValue.class);
	}

	@Override
	public Who getWho() {
		return Who.GATEWAY;
	}

	@Override
	public Where getWhere() {
		return new Where("", "");
	}

	public Date getDate() {
		return ((DateValue) get(GatewayStateName.DATE)).getDate();
	}

	public void setDate(Date newDate) {
		DateValue dv = new DateValue(newDate);
		set(GatewayStateName.DATE, dv);
	}
	
	public byte[] getIpAddress() {
		return ((IpAddressValue) get(GatewayStateName.IP_ADDRESS)).getIpAddress();
	}

	public byte[] getNetMask() {
		return ((IpAddressValue) get(GatewayStateName.NETMASK)).getIpAddress();
	}

	public String getDeviceType() {
		return get(GatewayStateName.MODEL).getValue();
	}

	public VersionValue getFirmwareVersion() {
		return (VersionValue) get(GatewayStateName.FIRMWARE_VERSION);
	}

	public DateValue getUpTime() {
		return (DateValue) get(GatewayStateName.UPTIME);
	}

	public VersionValue getKernelVersion() {
		return (VersionValue) get(GatewayStateName.KERNEL_VERSION);
	}

	public VersionValue getDistributionVersion() {
		return (VersionValue) get(GatewayStateName.DISTRIBUTION_VERSION);
	}
}
