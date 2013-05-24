package com.adgsoftware.mydomo.engine.controller.heating.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.heating.Offset;

public class SetOffset extends DimensionStatusImpl {

	private int LOCAL_OFFSET_POS = 0;
	
	public SetOffset() {
		super(new DimensionValue[] { 
				new DimensionValueImpl()  // OL: Local Offset
				},
				HeatingZone.HeatingZoneDimension.VALVE_STATUS.getCode()
		);
	}

	public Offset getLocalOffset() {
		String val = getStringValue(LOCAL_OFFSET_POS);
		if ("00".equals(val)) {
			return new Offset(Offset.Mode.ON, 0);
		} else if ("01".equals(val)) {
			return new Offset(Offset.Mode.ON, 1);
		} else if ("11".equals(val)) {
			return new Offset(Offset.Mode.ON, -1);
		} else if ("02".equals(val)) {
			return new Offset(Offset.Mode.ON, 2);
		} else if ("12".equals(val)) {
			return new Offset(Offset.Mode.ON, -2);
		} else if ("03".equals(val)) {
			return new Offset(Offset.Mode.ON, 3);
		} else if ("13".equals(val)) {
			return new Offset(Offset.Mode.ON, -3);
		} else if ("4".equals(val)) {
			return new Offset(Offset.Mode.OFF, 0);
		} else if ("5".equals(val)) {
			return new Offset(Offset.Mode.PROTECTION, 0);
		} else {
			return null;
		}
	}
}
