package com.adgsoftware.mydomo.engine.oldcontroller.heating;

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


import java.util.Date;

import com.adgsoftware.mydomo.engine.oldconnector.CommandResult;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionStatusListener;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;
import com.adgsoftware.mydomo.engine.oldcontroller.heating.dimension.EndDateHoliday;

public class HeatingCentral extends HeatingZone {

	/** uuid */
	private static final long serialVersionUID = 1L;

	// HEATING
	public enum HeatingStatus implements Status {
		HEATING_CONDITIONING_ON("0"),
		HEATING_HEATING_ON("1"),
		HEATING_ANTI_FREEZE_ON("102"),
		HEATING_THERMAL_PROTECTION("202"), // TODO What means this??? => protection against too hot?
		HEATING_GENERIC_PROTECTION("302"),
		HEATING_HEATING_OFF("103"),
		HEATING_CONDITIONING_OFF("203"),
		HEATING_OFF("303"),
		HEATING_HEATING_MANUAL_ADJUSTMENT_MODE("110"),
		HEATING_CONDITIONING_MANUAL_ADJUSTMENT_MODE("210"),
		HEATING_GENERIC_MANUAL_ADJUSTMENT_MODE("310"),
		HEATING_HEATING_PROGRAMMING_MODE("111"),
		HEATING_CONDITIONING_PROGRAMMING_MODE("211"),
		HEATING_GENERIC_PROGRAMMING_MODE("311"),
		HEATING_HEATING_HOLIDAY_DAILY("115"),
		HEATING_CONDITIONING_HOLIDAY_DAILY("215"),
		HEATING_GENERIC_HOLIDAY_DAILY("315");
		
		private String code = "";
		private HeatingStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
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
		
		public static HeatingZoneDimension fromValue(String code) {
			for (HeatingZoneDimension gd : HeatingZoneDimension.values()) {
				if (gd.getCode().equals(code))
					return gd;
			}
			
			return null;
		}
	}
	
	// Set the zone number! After this can be change depending action...
	@Override
	public void setWhere(String newValue) {
		super.setWhere("0");
		
		// SetEndDateHoliday
		getDimensionStatus(EndDateHoliday.class, new DimensionStatusListener<EndDateHoliday>() {

			@Override
			public void onDimensionStatus(EndDateHoliday status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
	}
	
	public Date getEndDateHoliday() {
		EndDateHoliday dt = (EndDateHoliday) getDimensionStatusFromCache(CentralHeatingDimension.END_DATE_HOLIDAY.getCode());
		if (dt == null) {
			return null;
		}
		return dt.getDate();
	}

	public void setEndDateHoliday(Date endDate) {
		EndDateHoliday edh = new EndDateHoliday();
		edh.setTime(endDate);
		this.setDimensionStatus(edh);
	}
}
