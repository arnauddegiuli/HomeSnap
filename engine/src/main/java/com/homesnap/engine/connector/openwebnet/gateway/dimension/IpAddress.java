package com.homesnap.engine.connector.openwebnet.gateway.dimension;

import java.util.logging.Level;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
import com.homesnap.engine.controller.gateway.IncorrectIpV4AddressException;
import com.homesnap.engine.controller.what.impl.IpAddressValue;

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


public class IpAddress extends DimensionStatusImpl<IpAddressValue> {

	private Log log = new Log();

	public IpAddress() {
		super(new DimensionValue[] {
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl()
				}, GatewayDimension.IP_ADDRESS.getCode());
	}

	@Override
	public IpAddressValue getStateValue() {
		IpAddressValue ip = new IpAddressValue();
		try {
			ip.setIpAddress(
				new byte[] {
					getByteValue(0),
					getByteValue(1),
					getByteValue(2),
					getByteValue(3)
			}
					);
		} catch (IncorrectIpV4AddressException e) {
			log.log(Session.Server, Level.SEVERE, "Wrong IP."); // Impossible normally...
		}
		
		return ip;
	}

	@Override
	public void setValueList(IpAddressValue value) {
		byte[] address = value.getIpAddress();
		setByteValue(address[0], 0, 0);
		setByteValue(address[1], 1, 0);
		setByteValue(address[2], 2, 0);
		setByteValue(address[3], 3, 0);
	}
}
