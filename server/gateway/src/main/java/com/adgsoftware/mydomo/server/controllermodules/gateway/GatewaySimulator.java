package com.adgsoftware.mydomo.server.controllermodules.gateway;

/*
 * #%L
 * MyDomoServerGatewayModule
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


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.gateway.Version;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.DateTime;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.DistributionVersion;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.FirmwareVersion;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.IpAddress;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Model;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Time;
import com.adgsoftware.mydomo.server.controllermodules.ControllerDimensionSimulator;

public class GatewaySimulator implements ControllerDimensionSimulator {

	
	private static final String GATEWAY_ADDRESS = "1";
	private static Hashtable<String, List<DimensionValue>> dimensionCache = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	@Override
	public String execute(String command) {
		String what = Command.getDimensionFromCommand(command);

		if (Gateway.GatewayDimension.TIME.getCode().equals(what)) {
			// define time... nothing to do except if you want change your
			// system hours
			return Command.ACK;
		} else if (Gateway.GatewayDimension.DATE.getCode().equals(
				what)) {
			// define date... nothing to do except if you want change your
			// system hours
			return Command.ACK;
		} else if (Gateway.GatewayDimension.DATETIME.getCode().equals(
				what)) {
			// define date... nothing to do except if you want change your
			// system hours
			return Command.ACK;
		} else if (Gateway.GatewayDimension.IP_ADDRESS.getCode().equals(
				what)) {
			String where = GATEWAY_ADDRESS; // Gateway has no address! => can only manage connected gateway
			String dimension = Command.getDimensionFromCommand(command);

			dimensionCache.put(where + "-" + dimension,
					Command.getDimensionListFromCommand(command));
			return Command.ACK;
		} else {
			return Command.NACK;
		}
	}

	@Override
	public String status(String command) {

		String where = GATEWAY_ADDRESS;
		String dimensionStr = Command.getDimensionFromCommand(command);
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
				InetAddress a = getIp();
				if (a!=null) { 
					i.setIpAddress(a.getAddress());
				} else {
					i.setIpAddress(new byte[] {Byte.parseByte("120"), Byte.parseByte("120"), 0, 1});		
				}
				dimensionList = i.getValueList();
			}
		} else if (Gateway.GatewayDimension.NETMASK.getCode().equals(
				dimensionStr)) {
			dimensionList = dimensionCache.get(where
					+ "-" + dimensionStr);
			if (dimensionList == null) {
				IpAddress i = new IpAddress();
				byte[] a = getNetmask();
				if (a!=null) { 
					i.setIpAddress(a);
				} else {
					i.setIpAddress(new byte[] {Byte.parseByte("120"), Byte.parseByte("120"), 0, 1});		
				}
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
		} else if (Gateway.GatewayDimension.DISTRIBUTION_VERSION.getCode().equals(
				dimensionStr)) {
			DistributionVersion f = new DistributionVersion();
			Version version = new Version();
			version.setBuild(19);
			version.setRelease(05);
			version.setVersion(78);
			f.setVersion(version);
			dimensionList = f.getValueList();
		} else if (Gateway.GatewayDimension.KERNEL_VERSION.getCode().equals(
				dimensionStr)) {
			FirmwareVersion f = new FirmwareVersion();
			Version version = new Version();
			version.setBuild(19);
			version.setRelease(05);
			version.setVersion(78);
			f.setVersion(version);
			dimensionList = f.getValueList();
		} else if (Gateway.GatewayDimension.MODEL.getCode().equals(
				dimensionStr)) {
			Model f = new Model();
			f.setModel(Model.ADGTESTSERVER);
			dimensionList = f.getValueList();
		} else if (Gateway.GatewayDimension.UPTIME.getCode().equals(
				dimensionStr)) {
			DateTime dt = new DateTime();
			dt.setTime(new Date());
			dimensionList = dt.getValueList();
		}  else {
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
	
	private InetAddress getIp() {
		
		InetAddress result = null;
		
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); 
			while (e.hasMoreElements()){ 
			  Enumeration<InetAddress> i = e.nextElement().getInetAddresses(); 
			  while (i.hasMoreElements()){ 
			    InetAddress a = i.nextElement(); 
			    if (a.isLoopbackAddress()) {
			    	result = a;
			    } else if (a.getAddress().length == 4) {
			    	return a;
			    }
			  } 
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	    
	    return result;
	}
	
	private byte[] getNetmask() {
		
		short prefix = 24;
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); 
			while (e.hasMoreElements()){ 
			  Enumeration<InetAddress> i = e.nextElement().getInetAddresses(); 
			  while (i.hasMoreElements()){ 
			    InetAddress a = i.nextElement(); 
			    if (a.isLoopbackAddress()) {
			    	prefix = NetworkInterface.getByInetAddress(a).getInterfaceAddresses().get(0).getNetworkPrefixLength();
			    } else {
			    	prefix = NetworkInterface.getByInetAddress(a).getInterfaceAddresses().get(0).getNetworkPrefixLength();
			    	break;
			    }
			  } 
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	    
		if (prefix == 8) {
			return new byte[] {Byte.parseByte("-1"), Byte.parseByte("0"), Byte.parseByte("0"), Byte.parseByte("0")};
		} else if (prefix == 16) {
			return new byte[] {Byte.parseByte("-1"), Byte.parseByte("-1"), Byte.parseByte("0"), Byte.parseByte("0")};
		} else /*if (prefix == 24)*/ {
			return new byte[] {Byte.parseByte("-1"), Byte.parseByte("-1"), Byte.parseByte("-1"), Byte.parseByte("0")};
		}
		
	}
	
	
}
