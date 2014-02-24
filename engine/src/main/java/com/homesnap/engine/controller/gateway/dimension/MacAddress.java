package com.homesnap.engine.controller.gateway.dimension;

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;

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


public class MacAddress extends DimensionStatusImpl {

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

	public byte[] getIpAddress() {
		byte[] address = new byte[] {
				getByteValue(0),
				getByteValue(1),
				getByteValue(2),
				getByteValue(3),
				getByteValue(4),
				getByteValue(5)
		};
		
		return address;
	}

	public void setIpAddress(byte[] address) {
		setByteValue(address[0], 0, 0);
		setByteValue(address[1], 1, 0);
		setByteValue(address[2], 2, 0);
		setByteValue(address[3], 3, 0);
		setByteValue(address[4], 4, 0);
		setByteValue(address[5], 5, 0);
	}
}
