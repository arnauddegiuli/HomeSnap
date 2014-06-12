//package com.homesnap.engine.test;
//
///*
// * #%L
// * MyDomoEngine
// * %%
// * Copyright (C) 2011 - 2013 A. de Giuli
// * %%
// * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
// * 
// *     MyDomo is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     (at your option) any later version.
// * 
// *     MyDomo is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU General Public License for more details.
// * 
// *     You should have received a copy of the GNU General Public License
// *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
// * #L%
// */
//
//
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.homesnap.engine.controller.gateway.Gateway;
//import com.homesnap.engine.controller.what.impl.VersionValue;
//import com.homesnap.engine.services.ControllerService;
//import com.homesnap.engine.services.impl.OpenWebNetControllerService;
//
//public class GatewayTest {
//
//	private ControllerService s = new OpenWebNetControllerService("localhost", 1234, 12345);
//	
//	@Test
//	public void gatewayModelTest() {
//		
//		final Gateway gateway = s.createController(Gateway.class, "");
//		
//		Assert.assertNotNull(gateway.getDeviceType());
//		Assert.assertEquals("ADG test Server", gateway.getDeviceType());
//
//		Assert.assertNotNull(gateway.getDate());
////		Assert.assertEquals("ADG test Server", gateway.getDeviceType());
//
//		VersionValue v = gateway.getDistributionVersion();
//		Assert.assertNotNull(v);
//		Assert.assertEquals(19, v.getVersion());
//		Assert.assertEquals(5, v.getRelease());
//		Assert.assertEquals(78, v.getBuild());
//
//		v = gateway.getFirmwareVersion();
//		Assert.assertNotNull(v);
//		Assert.assertEquals(19, v.getVersion());
//		Assert.assertEquals(5, v.getRelease());
//		Assert.assertEquals(78, v.getBuild());
//
//		byte[] ip = gateway.getIpAddress();
//		byte[] bip = getIp();
//		Assert.assertNotNull(ip);
//		Assert.assertEquals(ip[0], bip[0]);
//		Assert.assertEquals(ip[1], bip[1]);
//		Assert.assertEquals(ip[2], bip[2]);
//		Assert.assertEquals(ip[3], bip[3]);
//
//		v = gateway.getKernelVersion();
//		Assert.assertNotNull(v);
//		Assert.assertEquals(19, v.getVersion());
//		Assert.assertEquals(5, v.getRelease());
//		Assert.assertEquals(78, v.getBuild());
//
//		byte[] mask = gateway.getNetMask();
//		byte[] bmask = getNetmask();
//		Assert.assertNotNull(ip);
//		Assert.assertEquals(mask[0], bmask[0]);
//		Assert.assertEquals(mask[1], bmask[1]);
//		Assert.assertEquals(mask[2], bmask[2]);
//		Assert.assertEquals(mask[3], bmask[3]);
////		Assert.assertNotNull(gateway.getUpTime());
////		Assert.assertEquals("ADG test Server", gateway.getDeviceType());
//		
//	}
//	
//	private byte[] getIp() {
//		
//		InetAddress result = null;
//		
//		try {
//			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); 
//			while (e.hasMoreElements()){ 
//			  Enumeration<InetAddress> i = e.nextElement().getInetAddresses(); 
//			  while (i.hasMoreElements()){ 
//			    InetAddress a = i.nextElement(); 
//			    if (a.isLoopbackAddress()) {
//			    	result = a;
//			    } else if (a.getAddress().length == 4) {
//			    	return a.getAddress();
//			    }
//			  } 
//			}
//		} catch (SocketException e) {
//			e.printStackTrace();
//		}
//
//		return result.getAddress();
//	}
//	
//	private byte[] getNetmask() {
//		
//		short prefix = 24;
//		try {
//			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); 
//			while (e.hasMoreElements()){ 
//			  Enumeration<InetAddress> i = e.nextElement().getInetAddresses(); 
//			  while (i.hasMoreElements()){ 
//			    InetAddress a = i.nextElement(); 
//			    if (a.isLoopbackAddress()) {
//			    	prefix = NetworkInterface.getByInetAddress(a).getInterfaceAddresses().get(0).getNetworkPrefixLength();
//			    } else {
//			    	prefix = NetworkInterface.getByInetAddress(a).getInterfaceAddresses().get(0).getNetworkPrefixLength();
//			    	break;
//			    }
//			  } 
//			}
//		} catch (SocketException e) {
//			e.printStackTrace();
//		}
//	    
//		if (prefix == 8) {
//			return new byte[] {Byte.parseByte("-1"), Byte.parseByte("0"), Byte.parseByte("0"), Byte.parseByte("0")};
//		} else if (prefix == 16) {
//			return new byte[] {Byte.parseByte("-1"), Byte.parseByte("-1"), Byte.parseByte("0"), Byte.parseByte("0")};
//		} else /*if (prefix == 24)*/ {
//			return new byte[] {Byte.parseByte("-1"), Byte.parseByte("-1"), Byte.parseByte("-1"), Byte.parseByte("0")};
//		}
//		
//	}
//}
