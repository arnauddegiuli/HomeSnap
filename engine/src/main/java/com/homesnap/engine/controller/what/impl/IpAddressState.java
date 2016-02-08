package com.homesnap.engine.controller.what.impl;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.homesnap.engine.controller.what.State;

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


public class IpAddressState implements State<Inet4Address> {

	private Inet4Address ipAdress;

	public IpAddressState(Inet4Address address) {
		setValue(address);
	}


	@Override
	public Inet4Address getValue() {
		return ipAdress;
	}

	@Override
	public void setValue(Inet4Address value) {
		this.ipAdress = value;
	}

	@Override
	public void fromString(String value) {
		try {
			Inet4Address.getByName(value);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return ipAdress.getHostAddress();
//		return "".concat(String.valueOf(address[0]))
//				.concat(".").concat(String.valueOf(address[1]))
//				.concat(".").concat(String.valueOf(address[2]))
//				.concat(".").concat(String.valueOf(address[3]));
	}
}
