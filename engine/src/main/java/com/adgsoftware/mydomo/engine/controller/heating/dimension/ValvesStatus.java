package com.adgsoftware.mydomo.engine.controller.heating.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.heating.ValveStatusEnum;

public class ValvesStatus extends DimensionStatusImpl {

	private int CONDITIONING_VALVE_POS = 0;
	private int HEATING_VALVE_POS = 1;
	
	public ValvesStatus() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // CV: Conditioning Valve
				new DimensionValueImpl()  // HV: Heating Valve
				},
				HeatingZone.HeatingZoneDimension.VALVE_STATUS.getCode()
		);
	}

	public ValveStatusEnum getConditioningValveStatus() {
		return getStatus(
				getIntValue(CONDITIONING_VALVE_POS)
		);
	}
	
	public void setConditioningValveStatus(ValveStatusEnum value) {
		setIntValue(value.ordinal(), CONDITIONING_VALVE_POS, 1);
	}
	
	public ValveStatusEnum getHeatingValveStatus() {
		return getStatus(
				getIntValue(HEATING_VALVE_POS)
		);
	}
	
	public void setHeatingValveStatus(ValveStatusEnum value) {
		setIntValue(value.ordinal(), HEATING_VALVE_POS, 1);
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
}
