package com.homesnap.server.controllermodules.gateway;

/*
 * #%L
 * HomeSnapServerGatewayModule
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;
import com.homesnap.engine.connector.openwebnet.convert.OpenWebNetWho;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimensionConverter;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.DateTime;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.DistributionVersion;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.FirmwareVersion;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.IpAddress;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.MacAddress;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.Model;
import com.homesnap.engine.connector.openwebnet.gateway.dimension.Time;
import com.homesnap.engine.connector.openwebnet.parser.CommandParser;
import com.homesnap.engine.connector.openwebnet.parser.ParseException;

import com.homesnap.server.controllermodules.ControllerDimensionSimulator;

public class GatewaySimulator implements ControllerDimensionSimulator {

	
	private static final String GATEWAY_ADDRESS = "1";
	private static Hashtable<String, List<DimensionValue>> dimensionCache = new Hashtable<String, List<DimensionValue>>(); // where-dimension, dimensionList
	
	@Override
	public String execute(String command) {
		try {
			CommandParser parser = CommandParser.parse(command);
			String what = parser.getDimension();

			if (GatewayDimensionConverter.TIME.getCode().equals(what)) {
				// define time... nothing to do except if you want change your
				// system hours
				return OpenWebNetConstant.ACK;
			} else if (GatewayDimensionConverter.DATE.getCode().equals(
					what)) {
				// define date... nothing to do except if you want change your
				// system hours
				return OpenWebNetConstant.ACK;
			} else if (GatewayDimensionConverter.DATETIME.getCode().equals(
					what)) {
				// define date... nothing to do except if you want change your
				// system hours
				return OpenWebNetConstant.ACK;
			} else if (GatewayDimensionConverter.IP_ADDRESS.getCode().equals(
					what)) {
				String where = GATEWAY_ADDRESS; // Gateway has no address! => can only manage connected gateway
				String dimension = parser.getDimension();

				dimensionCache.put(where + "-" + dimension,
						parser.getDimensionList());
				return OpenWebNetConstant.ACK;
			} else {
				return OpenWebNetConstant.NACK;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
		}
	}

	@Override
	public String status(String command) {
		try {
			String where = GATEWAY_ADDRESS;
			CommandParser parser = CommandParser.parse(command);
			String dimensionStr = parser.getDimension();
			List<DimensionValue> dimensionList;
			if (GatewayDimensionConverter.TIME.getCode().equals(dimensionStr)) {
				Time t = new Time();
				t.setTime(new Date());
				dimensionList = t.getValueList();
			} else if (GatewayDimensionConverter.DATE.getCode().equals(dimensionStr)) {
				com.homesnap.engine.connector.openwebnet.gateway.dimension.Date d = new com.homesnap.engine.connector.openwebnet.gateway.dimension.Date();
				d.setDate(new Date());
				dimensionList = d.getValueList();
			} else if (GatewayDimensionConverter.DATETIME.getCode().equals(
					dimensionStr)) {
				DateTime dt = new DateTime();
				dt.setDate(new Date());
				dimensionList = dt.getValueList();
			} else if (GatewayDimensionConverter.IP_ADDRESS.getCode().equals(
					dimensionStr)) {
				dimensionList = dimensionCache.get(where
						+ "-" + dimensionStr);
				if (dimensionList == null) {
					IpAddress i = new IpAddress();
					InetAddress a = getIp();
					if (a!=null) {
						byte[] addr = a.getAddress();
						i.setIpAddress(addr[0],addr[1],addr[2],addr[3]);
					} else {
						i.setIpAddress(Byte.parseByte("120"), Byte.parseByte("120"), (byte) 0, (byte) 1);		
					}
					dimensionList = i.getValueList();
				}
			} else if (GatewayDimensionConverter.NETMASK.getCode().equals(
					dimensionStr)) {
				dimensionList = dimensionCache.get(where
						+ "-" + dimensionStr);
				if (dimensionList == null) {
					IpAddress i = new IpAddress();
					InetAddress a = getIp();
					if (a!=null) {
						byte[] addr = getNetmask();
						i.setIpAddress(addr[0],addr[1],addr[2],addr[3]);
					} else {
						i.setIpAddress(Byte.parseByte("120"), Byte.parseByte("120"), (byte) 0, (byte) 1);		
					}
					dimensionList = i.getValueList();
				}
			} else if (GatewayDimensionConverter.MAC_ADDRESS.getCode().equals(
					dimensionStr)) {
				dimensionList = dimensionCache.get(where
						+ "-" + dimensionStr);
				if (dimensionList == null) {
					MacAddress i = new MacAddress();
					byte[] a = getMacAddress();
					if (a!=null) { 
						i.setMacAddress(a[0], a[1], a[2], a[3], a[4], a[5]);
					} else {
						i.setMacAddress(Byte.parseByte("120"), Byte.parseByte("120"), (byte)0, (byte)1, (byte)0, (byte)1);		
					}
					
					dimensionList = i.getValueList();
				}
			} else if (GatewayDimensionConverter.FIRMWARE_VERSION.getCode().equals(
					dimensionStr)) {
				FirmwareVersion f = new FirmwareVersion();
				f.setDistributionVersion(19, 5, 78);
				dimensionList = f.getValueList();
			} else if (GatewayDimensionConverter.DISTRIBUTION_VERSION.getCode().equals(
					dimensionStr)) {
				DistributionVersion f = new DistributionVersion();
				f.setDistributionVersion(19, 5, 78);
				dimensionList = f.getValueList();
			} else if (GatewayDimensionConverter.KERNEL_VERSION.getCode().equals(
					dimensionStr)) {
				FirmwareVersion f = new FirmwareVersion();
				f.setDistributionVersion(19, 5, 78);
				dimensionList = f.getValueList();
			} else if (GatewayDimensionConverter.MODEL.getCode().equals(
					dimensionStr)) {
				Model f = new Model();
				f.setModel(Model.ADGTESTSERVER);
				dimensionList = f.getValueList();
			} else if (GatewayDimensionConverter.UPTIME.getCode().equals(
					dimensionStr)) {
				DateTime dt = new DateTime();
				dt.setDate(new Date());
				dimensionList = dt.getValueList();
			}  else {
				return OpenWebNetConstant.NACK;
			}
	
			if (dimensionList == null) {
				dimensionList = new ArrayList<DimensionValue>();
				dimensionCache.put(where + "-"
						+ dimensionStr, dimensionList);
			}
			StringBuilder sb = new StringBuilder();
			for (DimensionValue dimension : dimensionList) {
				sb.append(dimension.getValue());
				sb.append(OpenWebNetConstant.DIMENSION_SEPARATOR);
			}
			sb.setLength(sb.length() - 1);
			return MessageFormat.format(OpenWebNetConstant.DIMENSION_COMMAND, new Object[] {
					getWho(), where, dimensionStr, sb.toString() })
					+ OpenWebNetConstant.ACK;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getWho() {
		return OpenWebNetWho.WHO_GATEWAY.getValue();
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
	
	private byte[] getMacAddress() {
		try {
			byte[] prefix=null;
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); 
			while (e.hasMoreElements()){ 
			  Enumeration<InetAddress> i = e.nextElement().getInetAddresses(); 
			  while (i.hasMoreElements()){ 
			    InetAddress a = i.nextElement(); 
			    if (a.isLoopbackAddress()) {
			    	prefix = NetworkInterface.getByInetAddress(a).getHardwareAddress();
			    } else {
			    	prefix = NetworkInterface.getByInetAddress(a).getHardwareAddress();
			    	break;
			    }
			  } 
			}
			return prefix;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
