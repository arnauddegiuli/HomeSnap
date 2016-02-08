package com.homesnap.engine.connector.knxnetip.network;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public final class NetworkConfig {
	
	/** Singleton of the NetworkConfig */
	private static NetworkConfig instance = new NetworkConfig();
	static {
		instance.checkNetworkInterfaces();
	}
	
	/** All inet adresses of this computer */
	private Map<String, InetAddress> inetAdresses = new HashMap<String, InetAddress>();
	
	/** All network interfaces of this computer */
	private Map<InetAddress, NetworkInterface> networkInterfaces = new HashMap<InetAddress, NetworkInterface>();
	
	/**
	 * 
	 */
	private NetworkConfig() {
	}

	/**
	 * 
	 * @return
	 */
	public static NetworkConfig getInstance() {
		return instance;
	}

	/**
	 * Force scan of all network interfaces on this computer
	 * @throws SocketException
	 */
	public void checkNetworkInterfaces() {
		
		instance.inetAdresses.clear();
		instance.networkInterfaces.clear();
		
		try {
			Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allInterfaces.hasMoreElements()) {
				
				NetworkInterface networkInterface = allInterfaces.nextElement();
				Enumeration<InetAddress> interfaceAdresses = networkInterface.getInetAddresses();
				while (interfaceAdresses.hasMoreElements()) {
					InetAddress address = interfaceAdresses.nextElement();
					instance.inetAdresses.put(address.getHostAddress(), address);
					instance.networkInterfaces.put(address, networkInterface);
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException("An error occurs during analysis of network interfaces", e);
		}
	}
	
	/**
	 * Get all inet adresses of this computer
	 * @return An unmodifiable {@link Collection}
	 */
	public Collection<InetAddress> getInetAdresses() {
		return Collections.unmodifiableCollection(instance.inetAdresses.values());
	}
	
	/**
	 * 
	 * @param ipAddress
	 * @return
	 * @throws IllegalArgumentException
	 */
	public InetAddress getInetAdress(String ipAddress) {
		if (ipAddress == null) {
			throw new NullPointerException("Unable to find InetAddress with null IP.");
		}
		
		if (ipAddress.matches("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
			try {
				return InetAddress.getByName(ipAddress);
			} catch (UnknownHostException e) {
			}
		}
		throw new IllegalArgumentException("Invalid IP address "+ ipAddress);
	}

	/**
	 * Get the physical network interface which define an IP adress
	 * @param inetAddress
	 * @return The {@link NetworkInterface} if the IP address is defined on this computer or <code>null</code> otherwise
	 */
	public NetworkInterface getInterface(InetAddress inetAddress) {
		return instance.networkInterfaces.get(inetAddress);
	}

	/**
	 * 
	 * @param ipAddress
	 * @return
	 * @throws IllegalArgumentException
	 */
	public NetworkInterface getInterface(String ipAddress) {
		return instance.networkInterfaces.get(getInetAdress(ipAddress));
	}
}
