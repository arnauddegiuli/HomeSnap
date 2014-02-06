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


import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;
import com.adgsoftware.mydomo.engine.oldconnector.CommandResult;
import com.adgsoftware.mydomo.engine.oldconnector.ControllerType;
import com.adgsoftware.mydomo.engine.oldcontroller.ControllerDimension;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionStatusCallback;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionStatusListener;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;
import com.adgsoftware.mydomo.engine.oldcontroller.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.engine.oldcontroller.heating.dimension.MeasureTemperature;
import com.adgsoftware.mydomo.engine.oldcontroller.heating.dimension.SetOffset;
import com.adgsoftware.mydomo.engine.oldcontroller.heating.dimension.ValvesStatus;

public class HeatingZone extends ControllerDimension<HeatingZone.ZoneStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	private Log log = new Log();
	
	
	public enum ZoneStatus implements Status {
		
		HEATING_GENERIC_HOLIDAY_DAILY("315"); 
		
		private String code = "";
		private ZoneStatus(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public enum HeatingZoneDimension {
		MEASURE_TEMPERATURE("0"), 
		FAN_COIL_SPEED("11"),
		PROBE_STATUS("12"),
		LOCAL_OFFSET("13"),
		SET_TEMPERATURE("14"),
		VALVE_STATUS("19"),
		ACTUATOR_STATUS("20");
		
		private String code;
		private HeatingZoneDimension(String code) {
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
	
	private Object lock = new Object();
	
	@Override
	public String getWho() {
		return ControllerType.WHO_HEATING_ADJUSTMENT;
	}
	
	@Override
	public ZoneStatus getStatus(String code) {
		for (ZoneStatus status : ZoneStatus.values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}	
		return null;
	}
	
	// Set the zone number! After this can be change depending action...
	@Override
	public void setWhere(String newValue) {
		super.setWhere(newValue);
	}

	/**
	 * Synchronously method to get value. This means that if value is not in cache you need to wait from server response
	 * before get a return. This may be not good for your ergonomy.
	 * @param mode
	 * @return the desired temperature
	 */
	public DesiredTemperature getDesiredTemperature(HeatingModeEnum mode) {
		getDesiredTemperature(mode, new DimensionStatusCallback<DesiredTemperature>() {
			@Override
			public void value(DesiredTemperature value) {
				synchronized (lock) { lock.notify(); }
			}
		});

		try {
			synchronized (lock) { lock.wait(1000); } // Just wait 1000 in case of notify is faster than wait...
		} catch (InterruptedException e) {
			log.severe(Session.Command, e.getMessage());
		}
		return (DesiredTemperature) getDimensionStatusFromCache(HeatingZoneDimension.SET_TEMPERATURE.getCode());
	}
	
	/**
	 * Asynchronously method to get value.
	 * @param mode
	 * @param callback the method to call when result will be available
	 */
	public void getDesiredTemperature(final HeatingModeEnum mode, final DimensionStatusCallback<DesiredTemperature> callback) {	
		DesiredTemperature dt = (DesiredTemperature) getDimensionStatusFromCache(HeatingZoneDimension.SET_TEMPERATURE.getCode());
		if (dt == null) {
			getDimensionStatus(DesiredTemperature.class, new DimensionStatusListener<DesiredTemperature>() {
				@Override
				public void onDimensionStatus(DesiredTemperature status, CommandResult result) {
					changeDimensionStatus(status);
					callback.value(status);
				}
			});

		} else {
			callback.value(dt);
		}
	}
	
	/**
	 * Define the desired temperature. Value can be not be change instantly: it will send order
	 * to server. Only when monitor session will get the change, this will affect this controller.
	 * @param temperature
	 * @param mode
	 */
	public void setDesiredTemperature(double temperature, HeatingModeEnum mode) {
		DesiredTemperature t = new DesiredTemperature();
		t.setDesiredTemperature(temperature);
		t.setMode(mode.ordinal()+1);
		this.setDimensionStatus(t);
	}
	
	/**
	 * A
	 * @param callback
	 */
	public void getMeasureTemperature(final DimensionStatusCallback<MeasureTemperature> callback) {
		getDimensionStatus(MeasureTemperature.class, new DimensionStatusListener<MeasureTemperature>() {
			@Override
			public void onDimensionStatus(MeasureTemperature status,
					CommandResult result) {
				callback.value(status);
			}
			
		});
	}

	public void getValvesStatus(final DimensionStatusCallback<ValvesStatus> callback) {
		getDimensionStatus(ValvesStatus.class, new DimensionStatusListener<ValvesStatus>() {
			@Override
			public void onDimensionStatus(ValvesStatus status,
					CommandResult result) {
				callback.value(status);
			}
		});
	}
	
	public void getSetOffset(final DimensionStatusCallback<SetOffset> callback) {
		getDimensionStatus(SetOffset.class, new DimensionStatusListener<SetOffset>() {
			@Override
			public void onDimensionStatus(SetOffset status,
					CommandResult result) {
				callback.value(status);
			}
		});
	}
}