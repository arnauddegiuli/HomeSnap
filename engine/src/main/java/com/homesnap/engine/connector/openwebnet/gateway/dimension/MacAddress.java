//package com.homesnap.engine.connector.openwebnet.gateway.dimension;
//
//import java.util.logging.Level;
//
//import com.homesnap.engine.Log;
//import com.homesnap.engine.Log.Session;
//import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
//import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
//import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
//import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimensionConverter;
//import com.homesnap.engine.controller.what.StateValue;
//import com.homesnap.engine.controller.what.impl.IncorrectMacAddressException;
//import com.homesnap.engine.controller.what.impl.MacAddressState;
//
///*
// * #%L
// * HomeSnapEngine
// * %%
// * Copyright (C) 2011 - 2015 A. de Giuli
// * %%
// * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
// *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
// * 
// *     HomeSnap is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     (at your option) any later version.
// * 
// *     HomeSnap is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU General Public License for more details.
// * 
// *     You should have received a copy of the GNU General Public License
// *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
// * #L%
// */
//
//
//public class MacAddress extends DimensionStatusImpl<MacAddressState> {
//
//	private Log log = new Log();
//
//	public MacAddress() {
//		super(new DimensionValue[] {
//				new DimensionValueImpl(),
//				new DimensionValueImpl(),
//				new DimensionValueImpl(),
//				new DimensionValueImpl(),
//				new DimensionValueImpl(),
//				new DimensionValueImpl()
//				}, GatewayDimensionConverter.MAC_ADDRESS.getCode());
//	}
//
//	@Override
//	public MacAddressState getStateValue() {
//		MacAddressState address = new MacAddressState();
//		try {
//			address.setMacAddress( new byte[] {
//					getByteValue(0),
//					getByteValue(1),
//					getByteValue(2),
//					getByteValue(3),
//					getByteValue(4),
//					getByteValue(5)
//			});
//		} catch (IncorrectMacAddressException e) {
//			log.log(Session.Server, Level.SEVERE, "Wrong Mac address."); // Impossible normally...
//		}
//		return address;
//	}
//
//	@Override
//	public void setStateValue(StateValue value) {
//		// TODO throw new ReadOnlyException(); // read only dimension
//	}
//	
//	public void setMacAddress(byte addr1, byte addr2, byte addr3, byte addr4, byte addr5, byte addr6) {
//		setByteValue(addr1, 0, 0);
//		setByteValue(addr2, 1, 0);
//		setByteValue(addr3, 2, 0);
//		setByteValue(addr4, 3, 0);
//		setByteValue(addr5, 4, 0);
//		setByteValue(addr6, 5, 0);
//	}
//}
