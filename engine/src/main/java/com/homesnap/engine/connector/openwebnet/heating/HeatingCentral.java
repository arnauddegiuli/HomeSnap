package com.homesnap.engine.connector.openwebnet.heating;

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


import java.util.Date;

import com.homesnap.engine.controller.what.StateName;

public class HeatingCentral {

	// HEATING
	public enum HeatingStateName implements StateName {
		HEATING_CONDITIONING_ON("heating_conditioning_on"),
		HEATING_HEATING_ON("heating_heating_on"),
		HEATING_ANTI_FREEZE_ON("heating_anti_freeze_on"),
		HEATING_THERMAL_PROTECTION("heating_thermal_protection"), // TODO What means this??? => protection against too hot?
		HEATING_GENERIC_PROTECTION("heating_generic_protection"),
		HEATING_HEATING_OFF("heating_heating_off"),
		HEATING_CONDITIONING_OFF("heating_conditioning_off"),
		HEATING_OFF("heating_off"),
		HEATING_HEATING_MANUAL_ADJUSTMENT_MODE("blalba"),
		HEATING_CONDITIONING_MANUAL_ADJUSTMENT_MODE("blabla"),
		HEATING_GENERIC_MANUAL_ADJUSTMENT_MODE("blabla"),
		HEATING_HEATING_PROGRAMMING_MODE("blabla"),
		HEATING_CONDITIONING_PROGRAMMING_MODE("211"),
		HEATING_GENERIC_PROGRAMMING_MODE("311"),
		HEATING_HEATING_HOLIDAY_DAILY("115"),
		HEATING_CONDITIONING_HOLIDAY_DAILY("215"),
		HEATING_GENERIC_HOLIDAY_DAILY("315");

		private String name = "";
		private HeatingStateName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum CentralHeatingDimension {
		END_DATE_HOLIDAY("30"),
		END_HOUR_HOLIDAY("31");
		
		private String code;
		private CentralHeatingDimension(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	// Set the zone number! After this can be change depending action...

	
	public Date getEndDateHoliday() {
		return null;
	}

	public void setEndDateHoliday(Date endDate) {
	}
}
