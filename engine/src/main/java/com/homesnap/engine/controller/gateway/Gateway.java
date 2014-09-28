package com.homesnap.engine.controller.gateway;

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


import java.util.Date;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.gateway.stateName.GatewayStateName;
import com.homesnap.engine.controller.what.impl.DateValue;
import com.homesnap.engine.controller.what.impl.IpAddressValue;
import com.homesnap.engine.controller.what.impl.VersionValue;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;


public class Gateway extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;

	public Gateway() {
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
