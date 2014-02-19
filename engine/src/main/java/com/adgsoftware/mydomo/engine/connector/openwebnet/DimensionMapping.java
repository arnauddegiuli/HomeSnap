package com.adgsoftware.mydomo.engine.connector.openwebnet;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import com.adgsoftware.mydomo.engine.connector.openwebnet.dimension.DimensionStatus;
import com.adgsoftware.mydomo.engine.connector.openwebnet.gateway.GatewayDimension;
import com.adgsoftware.mydomo.engine.controller.what.State;

public class DimensionMapping {

	public static DimensionStatus convert(State state) {
		DimensionStatus ds = GatewayDimension.fromValue(state.getName()).createDimensionStatus();
//		ds.setValueList(dimensionList); TODO manage values
		return ds;
	}
	
	public static String getOpenWebNetCode(State state) {
		return GatewayDimension.fromValue(state.getName()).getCode();
	}
	
	public static State convert(DimensionStatus dimension) {
		return new State(GatewayDimension.fromValue(dimension.getCode()).getName(), null); // TODO manage value
		
	}
	
}
