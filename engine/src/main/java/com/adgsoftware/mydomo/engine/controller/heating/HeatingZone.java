package com.adgsoftware.mydomo.engine.controller.heating;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.DimensionStatusListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.MeasureTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.SetOffset;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.ValvesStatus;

public class HeatingZone extends ControllerDimension<HeatingZone.ZoneStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
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
		PROBE_STATUS("12"),
		LOCAL_OFFSET("13"),
		SET_TEMPERATURE("14"),
		VALVE_STATUS("19");
		
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
		return Command.WHO_HEATING_ADJUSTMENT;
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

	public Double getDesiredTemperature(HeatingModeEnum mode) {
		DesiredTemperature dt = (DesiredTemperature) getDimensionStatusFromCache(HeatingZoneDimension.SET_TEMPERATURE.getCode());
		if (dt == null) {
			getDimensionStatus(DesiredTemperature.class, new DimensionStatusListener<DesiredTemperature>() {
				@Override
				public void onDimensionStatus(DesiredTemperature status, CommandResult result) {
					changeDimensionStatus(status);
					synchronized (lock) { lock.notify(); }
				}
			});

			try {
				synchronized (lock) { lock.wait(); }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dt = (DesiredTemperature) getDimensionStatusFromCache(HeatingZoneDimension.SET_TEMPERATURE.getCode());
		}
		return dt == null ?  null : dt.getDesiredTemperature();
	}
	
	public synchronized void setDesiredTemperature(double temperature, HeatingModeEnum mode) {
		DesiredTemperature t = new DesiredTemperature();
		t.setDesiredTemperature(temperature);
		t.setMode(mode.ordinal()+1);
		this.setDimensionStatus(t);
	}
	
	public void getMeasureTemperature(DimensionStatusListener<MeasureTemperature> listener) {
		getDimensionStatus(MeasureTemperature.class, listener);
	}

	public void getValvesStatus(DimensionStatusListener<ValvesStatus> listener) {
		getDimensionStatus(ValvesStatus.class, listener);
	}
	
	public void getSetOffset(DimensionStatusListener<SetOffset> listener) {
		getDimensionStatus(SetOffset.class, listener);
	}
}