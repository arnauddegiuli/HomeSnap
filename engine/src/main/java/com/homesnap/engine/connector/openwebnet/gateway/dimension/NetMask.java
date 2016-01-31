package com.homesnap.engine.connector.openwebnet.gateway.dimension;

import java.util.logging.Level;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimensionConverter;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.impl.IncorrectIpV4AddressException;
import com.homesnap.engine.controller.what.impl.IpAddressValue;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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


public class NetMask extends DimensionStatusImpl<IpAddressValue> {

	private Log log = new Log();

	public NetMask() {
		super(new DimensionValue[] {
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl()
				}, GatewayDimensionConverter.NETMASK.getCode());
	}

	@Override
	public IpAddressValue getStateValue() {
		IpAddressValue address = new IpAddressValue();
		try {
			address.setIpAddress( new byte[] {
					getByteValue(0),
					getByteValue(1),
					getByteValue(2),
					getByteValue(3)
			});
		} catch (IncorrectIpV4AddressException e) {
			log.log(Session.Server, Level.SEVERE, "Wrong Netmask."); // Impossible normally...
		}
		return address;
	}

	@Override
	public void setStateValue(StateValue value) {
		// TODO throw new ReadOnlyException(); // read only dimension
	}

	public void setIpAddress(byte addr1, byte addr2, byte addr3, byte addr4) {
		setByteValue(addr1, 0, 0);
		setByteValue(addr2, 1, 0);
		setByteValue(addr3, 2, 0);
		setByteValue(addr4, 3, 0);
	}
}
