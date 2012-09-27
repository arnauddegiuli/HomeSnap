package com.adgsoftware.mydomo.engine.controller.gateway.dimension;

import com.adgsoftware.mydomo.engine.controller.DimensionStatusImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.DimensionValueImpl;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

public class NetMask extends DimensionStatusImpl {

	public NetMask() {
		super(new DimensionValue[] {
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl(),
				new DimensionValueImpl()
				}, Gateway.GatewayDimension.NETMASK.getCode());
	}

	public byte[] getNetMask() {
		byte[] address = new byte[] {
				getByteValue(0),
				getByteValue(1),
				getByteValue(2),
				getByteValue(3)
		};
		
		return address;
	}

	public void setNetMask(byte[] address) {
		setByteValue(address[0], 0, 0);
		setByteValue(address[1], 1, 0);
		setByteValue(address[2], 2, 0);
		setByteValue(address[3], 3, 0);
	}
}
