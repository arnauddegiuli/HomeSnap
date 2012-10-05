package com.adgsoftware.mydomo.server.commandmodules.gateway;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.gateway.Version;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.DateTime;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.FirmwareVersion;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.IpAddress;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Time;
import com.adgsoftware.mydomo.server.commandmodules.ControllerDimensionSimulator;

public class GatewaySimulator implements ControllerDimensionSimulator {

	private static Hashtable<String, List<DimensionValue>> dimensionCache = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	@Override
	public String execute(String command) {
		DimensionValue what = Command.getDimensionFromCommand(command);

		if (Gateway.GatewayDimension.TIME.getCode().equals(what.getValue())) {
			// define time... nothing to do except if you want change your
			// system hours
			return Command.ACK;
		} else if (Gateway.GatewayDimension.DATE.getCode().equals(
				what.getValue())) {
			// define date... nothing to do except if you want change your
			// system hours
			return Command.ACK;
		} else if (Gateway.GatewayDimension.DATETIME.getCode().equals(
				what.getValue())) {
			// define date... nothing to do except if you want change your
			// system hours
			return Command.ACK;
		} else if (Gateway.GatewayDimension.IP_ADDRESS.getCode().equals(
				what.getValue())) {
			String where = Command.getWhereFromCommand(command);
			String dimension = Command.getDimensionFromCommand(command)
					.getValue();

			dimensionCache.put(where + "-" + dimension,
					Command.getDimensionListFromCommand(command));
			return Command.ACK;
		} else {
			return Command.NACK;
		}
	}

	@Override
	public String status(String command) {

		String where = Command.getWhereFromCommand(command);
		String dimensionStr = Command.getDimensionFromCommand(command)
				.getValue();
		List<DimensionValue> dimensionList;
		if (Gateway.GatewayDimension.TIME.getCode().equals(dimensionStr)) {
			Time t = new Time();
			t.setTime(new Date());
			dimensionList = t.getValueList();
		} else if (Gateway.GatewayDimension.DATE.getCode().equals(dimensionStr)) {
			com.adgsoftware.mydomo.engine.controller.gateway.dimension.Date d = new com.adgsoftware.mydomo.engine.controller.gateway.dimension.Date();
			d.setTime(new Date());
			dimensionList = d.getValueList();
		} else if (Gateway.GatewayDimension.DATETIME.getCode().equals(
				dimensionStr)) {
			DateTime dt = new DateTime();
			dt.setTime(new Date());
			dimensionList = dt.getValueList();
		} else if (Gateway.GatewayDimension.IP_ADDRESS.getCode().equals(
				dimensionStr)) {
			dimensionList = dimensionCache.get(where
					+ "-" + dimensionStr);
			if (dimensionList == null) {
				IpAddress i = new IpAddress();
				i.setIpAddress(new byte[] {Byte.parseByte("120"), Byte.parseByte("120"), 0, 1});
				dimensionList = i.getValueList();
			}
		} else if (Gateway.GatewayDimension.FIRMWARE_VERSION.getCode().equals(
				dimensionStr)) {
			FirmwareVersion f = new FirmwareVersion();
			Version version = new Version();
			version.setBuild(19);
			version.setRelease(05);
			version.setVersion(78);
			f.setVersion(version);
			dimensionList = f.getValueList();
		} else {
			return Command.NACK;
		}

		if (dimensionList == null) {
			dimensionList = new ArrayList<DimensionValue>();
			dimensionCache.put(where + "-"
					+ dimensionStr, dimensionList);
		}
		StringBuilder sb = new StringBuilder();
		for (DimensionValue dimension : dimensionList) {
			sb.append(dimension.getValue());
			sb.append(Command.DIMENSION_SEPARATOR);
		}
		sb.setLength(sb.length() - 1);
		return MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {
				Command.WHO_GATEWAY, where, dimensionStr, sb.toString() })
				+ Command.ACK;
	}

	@Override
	public String getWho() {
		return Command.WHO_GATEWAY;
	}
}
