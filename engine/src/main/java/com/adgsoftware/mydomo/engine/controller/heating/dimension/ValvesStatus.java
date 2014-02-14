package com.adgsoftware.mydomo.engine.controller.heating.dimension;

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

import com.adgsoftware.mydomo.engine.controller.heating.ValveStatusEnum;
import com.adgsoftware.mydomo.engine.controller.what.StateValue;

public class ValvesStatus implements StateValue {

	private int CONDITIONING_VALVE_POS = 0;
	private int HEATING_VALVE_POS = 1;
	
	
	private ValveStatusEnum s;
	public ValvesStatus() {

	}

	public ValveStatusEnum getConditioningValveStatus() {
		return s;
	}
	
	public void setConditioningValveStatus(ValveStatusEnum value) {
		s = value;
	}
	
	public ValveStatusEnum getHeatingValveStatus() {
		return s;
	}
	
	public void setHeatingValveStatus(ValveStatusEnum value) {
		s = value;
	}
	
	private ValveStatusEnum getStatus(int value) {
		switch (value) {
		case 0:
			return ValveStatusEnum.OFF;

		case 1:
			return ValveStatusEnum.ON;

		case 2:
			return ValveStatusEnum.Opened;

		case 3:
			return ValveStatusEnum.Closed;

		case 4:
			return ValveStatusEnum.Stop;

		case 5:
			return ValveStatusEnum.OFF_Fan_Coil;

		case 6:
			return ValveStatusEnum.ON_Speed1;

		case 7:
			return ValveStatusEnum.ON_Speed2;

		case 8:
			return ValveStatusEnum.ON_Speed3;


		default:
			return ValveStatusEnum.Unknown;
		}
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
