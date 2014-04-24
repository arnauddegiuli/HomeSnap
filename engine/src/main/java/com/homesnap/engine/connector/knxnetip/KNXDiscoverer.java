package com.homesnap.engine.connector.knxnetip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.Discoverer;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import tuwien.auto.calimero.knxnetip.util.DeviceDIB;
import tuwien.auto.calimero.knxnetip.util.HPAI;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
import tuwien.auto.calimero.link.medium.TPSettings;

import com.homesnap.engine.connector.knxnetip.network.NetworkConfig;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXDiscoverer {
	
	/** */
	private static KNXDiscoverer instance = new KNXDiscoverer();
	
	/** */
	private Map<InetAddress, KNXnetIPConnectionSettings> knxInetAddresses = new HashMap<InetAddress, KNXnetIPConnectionSettings>();

	/**
	 * 
	 */
	private KNXDiscoverer() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static KNXDiscoverer getInstance() {
		return instance;
	}

	/**
	 * 
	 * @return
	 */
	public Map<InetAddress, KNXnetIPConnectionSettings> findKNXNetIPAddresses() {
		return findKNXNetIPAddresses(false);
	}
	
	/**
	 * 
	 * @param forceSearch
	 * @return
	 */
	public Map<InetAddress, KNXnetIPConnectionSettings> findKNXNetIPAddresses(boolean forceSearch) {
		
		synchronized (knxInetAddresses) {
			
			if (knxInetAddresses.isEmpty() || forceSearch) {
				knxInetAddresses.clear(); // Clear previous results
				
				// Get all bindables inet addresses of this computer
				List<InetAddress> localAddresses = new ArrayList<InetAddress>();
				for (InetAddress inetAddress : NetworkConfig.getInstance().getInetAdresses()) {
					if (!inetAddress.isLoopbackAddress()) {
						localAddresses.add(inetAddress);
					}
				}
				
				// Start a KNXDiscoveryWorker on each local IP address found
				List<KNXDiscoveryWorker> workers = new ArrayList<KNXDiscoveryWorker>();
				for (InetAddress localAddress : localAddresses) {
					
					KNXDiscoveryWorker worker = new KNXDiscoveryWorker(localAddress);
					workers.add(worker);
					worker.start();
				}
				
				// Wait the end of search of each worker in order to get all KNX NetIP devices found by IP address
				do { 
					Iterator<KNXDiscoveryWorker> iter = workers.iterator();
					while (iter.hasNext()) {
						
						KNXDiscoveryWorker worker = iter.next();
						if (worker.isAlive()) {
							continue;
						} else {
							for (KNXnetIPConnectionSettings knxSettings : worker.getConnectionSettings()) {
								knxInetAddresses.put(knxSettings.getRemoteAddress(), knxSettings);
							}
							iter.remove();
						}
					}
					if (!workers.isEmpty()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				} while (!workers.isEmpty());
			}
			return Collections.unmodifiableMap(knxInetAddresses);
		}
	}
	
	/**
	 * 
	 * @author DRIESBACH Olivier
	 * @version 1.0
	 * @since 1.0
	 */
	private class KNXDiscoveryWorker extends Thread {
		
		private InetAddress localAddress;
		private List<KNXnetIPConnectionSettings> connectionSettings;
		
		KNXDiscoveryWorker(InetAddress localAddress) {
			super("KNX discoverer on "+ localAddress.getHostName());
			this.localAddress = localAddress;
		}
		
		public List<KNXnetIPConnectionSettings> getConnectionSettings() {
			return connectionSettings;
		}
		
		@Override
		public void run() {
			
			Discoverer knxDiscoverer = null;
			try {
				knxDiscoverer = new Discoverer(localAddress, 0, false);
				NetworkInterface networkInterface = NetworkConfig.getInstance().getInterface(localAddress);
				knxDiscoverer.startSearch(0, networkInterface, 3, false);
				System.out.println("Start searching KNXnetIP devices on "+ localAddress.getHostAddress() +" ("+ networkInterface.getDisplayName() +")");
				while (knxDiscoverer.isSearching() && knxDiscoverer.getSearchResponses().length == 0) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
				}
			}
			catch (KNXException e) {
				// TODO Log
				e.printStackTrace();
			}
			
			connectionSettings = new ArrayList<KNXnetIPConnectionSettings>();
			for (SearchResponse response : knxDiscoverer.getSearchResponses()) {
				
				HPAI endPoint = response.getControlEndpoint();
				DeviceDIB netIPDevice = response.getDevice();
				KNXMediumSettings knxSettings = null;
				switch (netIPDevice.getKNXMedium()) {
				
					case KNXMediumSettings.MEDIUM_TP0:
					case KNXMediumSettings.MEDIUM_TP1: {
						knxSettings = new TPSettings(netIPDevice.getAddress(), (KNXMediumSettings.MEDIUM_TP1 == netIPDevice.getKNXMedium()));
						break;
					}
					case KNXMediumSettings.MEDIUM_PL110:
					case KNXMediumSettings.MEDIUM_PL132:
					case KNXMediumSettings.MEDIUM_RF: {
						// TODO Log
//						throw new KNXException("KNX settings not yet supported");
					}
					default: {
						// TODO Log
//						throw new KNXException("Unkwnon settings");
					}
				}
				
//				if (log.isDebugEnabled()) {
					System.out.println("Found device : "+ netIPDevice.getName()
							+" (IP Address: "+ endPoint.getAddress()
							+", Address: "+ netIPDevice.getAddress()
							+", serial-number: "+ netIPDevice.getSerialNumberString()
							+", MAC Address: "+ netIPDevice.getMACAddressString()
							+")");
//				}
				connectionSettings.add(new KNXnetIPConnectionSettings(localAddress, endPoint.getAddress(), endPoint.getPort(), knxSettings));
			}
		}
	}
}
