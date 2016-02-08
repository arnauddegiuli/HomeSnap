package com.homesnap.engine.connector.knxnetip;

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


import java.net.InetSocketAddress;

import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.KNXnetIPConnection;
import tuwien.auto.calimero.knxnetip.KNXnetIPRouter;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;

/**
 * Utility class to create KNX connections to a KNXnetIP device based on the KNX informations obtained by the KNXDiscoverer
 * 
 * @see KNXDiscoverer
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXConnection {
	
	/**
	 * 
	 * @param mode {@link KNXNetworkLinkIP#ROUTER} or {@link KNXNetworkLinkIP#TUNNEL} 
	 * @param knxConnectionSettings
	 * @return
	 * @throws KNXException
	 */
	public static KNXNetworkLink createConnection(int mode, KNXnetIPConnectionSettings knxConnectionSettings) throws KNXException {
		
		InetSocketAddress localSocket = null;
		InetSocketAddress remoteSocket = null;
		try {
			localSocket = new InetSocketAddress(knxConnectionSettings.getLocalAddress(), 0);
			if (KNXNetworkLinkIP.ROUTER == mode) {
				remoteSocket = new InetSocketAddress(KNXnetIPRouter.DEFAULT_MULTICAST, KNXnetIPConnection.IP_PORT);
			}
			else if (KNXNetworkLinkIP.TUNNEL == mode) {
				remoteSocket = new InetSocketAddress(knxConnectionSettings.getRemoteAddress(), KNXnetIPConnection.IP_PORT);
			}
			else {
				throw new IllegalArgumentException("Unknwon connection mode "+ mode +", only KNXNetworkLinkIP.TUNNEL or KNXNetworkLinkIP.ROUTER are supported.");
			}
			return new KNXNetworkLinkIP(mode, localSocket, remoteSocket, false, knxConnectionSettings.getSettings());
		} catch (Throwable t) { // TODO Create a ConnectException
			
			StringBuilder msg = new StringBuilder("Could not establish connection");
			if (localSocket != null) {
				msg.append(" from ").append(localSocket.getAddress() +":"+ localSocket.getPort());
				if (remoteSocket != null) {
					msg.append(" to ").append(remoteSocket.getAddress() +":"+ remoteSocket.getPort());
				}
			}
			throw new KNXException(msg.toString());
		}
	}
}
