package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class Model extends DimensionStatusImpl {
	
	private int MODEL_POS = 0;
	
	public Model() {
		super(new DimensionValue[] { 
				new DimensionValueImpl(), // Model
				},
			Gateway.GatewayDimension.MODEL.getCode()
		);
	}
	
	public String getModel() {
		int model = getIntValue(MODEL_POS);
		switch (model) {
		case 2:
			return "MHServer";

		case 4:
			return "MH200";
		case 6:
			return "F452";
		case 7:
			return "F452V";
		case 11:
			return "MHServer2";
		case 13:
			return "H4684";
		case 19:
			return "ADG test Server";
		default:
			return "Unknown";
		}
	}
}
