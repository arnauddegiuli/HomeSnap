package com.homesnap.engine.connector.openwebnet.gateway.dimension;

import java.util.logging.Level;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
import com.homesnap.engine.controller.what.impl.IncorrectMacAddressException;
import com.homesnap.engine.controller.what.impl.MacAddressValue;

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


public class MacAddress extends DimensionStatusImpl<MacAddressValue> {

	private Log log = new Log();

	public MacAddress() {
		super(new DimensionValue[] {
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl()
				}, GatewayDimension.MAC_ADDRESS.getCode());
	}

	@Override
	public MacAddressValue getStateValue() {
		MacAddressValue address = new MacAddressValue();
		try {
			address.setMacAddress( new byte[] {
					getByteValue(0),
					getByteValue(1),
					getByteValue(2),
					getByteValue(3),
					getByteValue(4),
					getByteValue(5)
			});
		} catch (IncorrectMacAddressException e) {
			log.log(Session.Server, Level.SEVERE, "Wrong Mac address."); // Impossible normally...
		}
		return address;
	}

	@Override
	public void setValueList(MacAddressValue value) {
		// TODO normalement impossible => lecture seule
		byte[] address = value.getMacAddress();
		setByteValue(address[0], 0, 0);
		setByteValue(address[1], 1, 0);
		setByteValue(address[2], 2, 0);
		setByteValue(address[3], 3, 0);
		setByteValue(address[4], 4, 0);
		setByteValue(address[5], 5, 0);
	}
}
