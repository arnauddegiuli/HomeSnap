package com.homesnap.engine.connector.openwebnet;

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

import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.who.Who;

public class DimensionMapping {

	public static DimensionStatus convert(Who w, State state) {
		DimensionStatus ds = GatewayDimension.fromValue(state.getName()).createDimensionStatus();
//		ds.setValueList(dimensionList); TODO manage values
		return ds;
	}
	
	public static String getOpenWebNetCode(State state) {
		return GatewayDimension.fromValue(state.getName()).getCode();
	}
	
	public static State convert(String w, DimensionStatus dimension) {
		// TODO transform OpenWebNetWho en enum
		if (OpenWebNetWho.WHO_HEATING_ADJUSTMENT.equals(w)) {
			return new State(HeatingZoneDimension.fromValue(dimension.getCode()).getName(), null); // TODO manage value
		} else {
			return new State(GatewayDimension.fromValue(dimension.getCode()).getName(), null); // TODO manage value
		}
	}
	
}
