package com.adgsoftware.mydomo.engine.controller.outlet;

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


import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Outlet extends Controller<Outlet.OutletStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	// OUTLET
	public enum OutletStatus implements Status {
		OUTLET_OFF("0"),
		OUTLET_ON("1");
		
		private String code = "";
		private OutletStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public Outlet() {
	}
	
	@Override
	public String getWho() {
		return Command.WHO_POWER_MANAGEMENT;
	}
	
	@Override
	public OutletStatus getStatus(String code) {
		if (code == null) 
			return null;
		for (OutletStatus status : OutletStatus.values()) {
			if (code.equals(status.getCode())) {
				return status;
			}
		}	
		return null;
	}

}