package com.homesnap.engine.controller.gateway;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
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


import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.gateway.stateName.GatewayStateName;
import com.homesnap.engine.controller.what.impl.DateState;
import com.homesnap.engine.controller.what.impl.IpAddressState;
import com.homesnap.engine.controller.what.impl.StringState;
import com.homesnap.engine.controller.what.impl.VersionState;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;


public class Gateway extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;

	@Override
	public Who getWho() {
		return Who.GATEWAY;
	}

	@Override
	public Where getWhere() {
		return new Where("", "");
	}

	public Date getDate() {
		return ((DateState) get(GatewayStateName.date.name())).getValue();
	}

	public void setDate(Date newDate) {
		set(GatewayStateName.date.name(), new DateState(newDate));
	}
	
	public Inet4Address getIpAddress() {
		return ((IpAddressState) get(GatewayStateName.ip_address.name())).getValue();
	}

	public Inet4Address getNetMask() {
		return ((IpAddressState) get(GatewayStateName.netmask.name())).getValue();
	}

	public String getDeviceType() {
		return ((StringState) get(GatewayStateName.MODEL.name())).getValue();
	}

	public VersionState getFirmwareVersion() {
		return (VersionState) get(GatewayStateName.FIRMWARE_VERSION.name());
	}

	public DateState getUpTime() {
		return (DateState) get(GatewayStateName.UPTIME.name());
	}

	public VersionState getKernelVersion() {
		return (VersionState) get(GatewayStateName.KERNEL_VERSION.name());
	}

	public VersionState getDistributionVersion() {
		return (VersionState) get(GatewayStateName.DISTRIBUTION_VERSION.name());
	}

	@Override
	public List<String> getStateList() {
		List<String> result = new ArrayList<String>();
		GatewayStateName[] list = GatewayStateName.values();
		for (int i = 0; i < list.length; i++) {
			result.add(list[i].name());
		};
		return result;
	}
}
