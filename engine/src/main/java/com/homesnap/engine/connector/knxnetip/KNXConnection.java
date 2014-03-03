package com.homesnap.engine.connector.knxnetip;

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
