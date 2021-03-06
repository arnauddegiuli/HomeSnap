//package com.homesnap.engine.connector.openwebnet.gateway.dimension;
//
//import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatusImpl;
//import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
//import com.homesnap.engine.connector.openwebnet.dimension.DimensionValueImpl;
//import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimensionConverter;
//import com.homesnap.engine.controller.what.StateValue;
//import com.homesnap.engine.controller.what.impl.StringState;
//
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
//
//
//public class Model extends DimensionStatusImpl<StringState> {
//	
//	private int MODEL_POS = 0;
//	
//	public static final int MHSERVER = 2;
//	public static final int MH200 = 4;
//	public static final int F452 = 6;
//	public static final int F452V = 7;
//	public static final int MHServer2 = 11;
//	public static final int H4684 = 13;
//	public static final int ADGTESTSERVER = 19;
//	
//	
//	public Model() {
//		super(new DimensionValue[] { 
//				new DimensionValueImpl(), // Model
//				},
//			GatewayDimensionConverter.MODEL.getCode()
//		);
//	}
//
//	@Override
//	public StringState getStateValue() {
//		int model = getIntValue(MODEL_POS);
//		switch (model) {
//		case MHSERVER:
//			return new StringState("MHServer");
//		case MH200:
//			return new StringState("MH200");
//		case F452:
//			return new StringState("F452");
//		case F452V:
//			return new StringState("F452V");
//		case MHServer2:
//			return new StringState("MHServer2");
//		case H4684:
//			return new StringState("H4684");
//		case ADGTESTSERVER:
//			return new StringState("ADG test Server");
//		default:
//			return new StringState("Unknown");
//		}
//	}
//
//	@Override
//	public void setStateValue(StateValue value) {
//		// TODO throw new ReadOnlyException(); // read only dimension
//	}
//	
//	public void setModel(int value) {
//		setIntValue(value, MODEL_POS, 0);
//	}
//}
