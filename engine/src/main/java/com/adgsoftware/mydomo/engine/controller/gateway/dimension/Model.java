package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class Model extends DimensionStatusImpl {
	
	private int MODEL_POS = 0;
	
	public static final int MHSERVER = 2;
	public static final int MH200 = 4;
	public static final int F452 = 6;
	public static final int F452V = 7;
	public static final int MHServer2 = 11;
	public static final int H4684 = 13;
	public static final int ADGTESTSERVER = 19;
	
	
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
		case MHSERVER:
			return "MHServer";
		case MH200:
			return "MH200";
		case F452:
			return "F452";
		case F452V:
			return "F452V";
		case MHServer2:
			return "MHServer2";
		case H4684:
			return "H4684";
		case ADGTESTSERVER:
			return "ADG test Server";
		default:
			return "Unknown";
		}
	}
	
	public void setModel(int model) {
		setIntValue(model, MODEL_POS, 0);
	}
}
