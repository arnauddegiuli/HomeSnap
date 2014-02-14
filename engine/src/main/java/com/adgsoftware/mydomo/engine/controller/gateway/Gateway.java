package com.adgsoftware.mydomo.engine.controller.gateway;

/*
 * #%L
 * MyDomoEngine
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


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.DateValue;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.IpAddress;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.VersionValue;
import com.adgsoftware.mydomo.engine.controller.what.StateName;
import com.adgsoftware.mydomo.engine.controller.what.StateValue;
import com.adgsoftware.mydomo.engine.controller.what.impl.StringValue;
import com.adgsoftware.mydomo.engine.controller.who.Who;


public class Gateway extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	public enum GatewayStateName implements StateName {
		DATE("date"),
		DATETIME("datetime"),
		DISTRIBUTION_VERSION("distributionVersion"),
		FIRMWARE_VERSION("firmwareVersion"),
		IP_ADDRESS("ipAddress"),
		KERNEL_VERSION("kernelVersion"),
		MAC_ADDRESS("macAddress"),
		MODEL("model"),
		NETMASK("netMask"),
		TIME("time"),
		UPTIME("uptime");
		
		private String name;
		private GatewayStateName(String name) {
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public static GatewayStateName fromValue(String code) {
			for (GatewayStateName gd : GatewayStateName.values()) {
				if (gd.getName().equals(code))
					return gd;
			}
			return null;
		}
	}
	
	public Gateway() {
	}
	
	@Override
	protected Map<StateName, Class<? extends StateValue>> getSupportedStateTypes() {
		Map<StateName, Class<? extends StateValue>> m = new HashMap<StateName, Class<? extends StateValue>>();
		m.put(GatewayStateName.DATE, DateValue.class);
		m.put(GatewayStateName.DATETIME, DateValue.class);
		m.put(GatewayStateName.DISTRIBUTION_VERSION, VersionValue.class);
		m.put(GatewayStateName.FIRMWARE_VERSION, VersionValue.class);
		m.put(GatewayStateName.IP_ADDRESS, IpAddress.class);
		m.put(GatewayStateName.KERNEL_VERSION, VersionValue.class);
		m.put(GatewayStateName.MAC_ADDRESS, IpAddress.class);
		m.put(GatewayStateName.MODEL, StringValue.class);
		m.put(GatewayStateName.NETMASK, IpAddress.class);
		m.put(GatewayStateName.TIME, DateValue.class);
		m.put(GatewayStateName.UPTIME, DateValue.class);
		
		return m;
	}

	@Override
	public Who getWho() {
		return Who.GATEWAY;
	}

	public Date getDate() {
		return ((DateValue) get(GatewayStateName.DATE)).getDate();
	}

	public void setDate(java.util.Date newDate) {
		DateValue dv = new DateValue();
		dv.setDate(newDate);
		set(GatewayStateName.DATE, dv);
	}
	
	public byte[] getIpAddress() {
		return ((IpAddress) get(GatewayStateName.IP_ADDRESS)).getIpAddress();
	}
	
	public byte[] getNetMask() {
		return ((IpAddress) get(GatewayStateName.NETMASK)).getIpAddress();
	}
	
	public String getDeviceType() {
		return get(GatewayStateName.MODEL).getValue();
	}
	
	public Version getFirmwareVersion() {
		return ((VersionValue) get(GatewayStateName.FIRMWARE_VERSION)).getVersion();
	}
	
	public void getUpTime() {
		// TODO finir
		get(GatewayStateName.UPTIME);
	}

	public Version getKernelVersion() {
		return ((VersionValue) get(GatewayStateName.KERNEL_VERSION)).getVersion();
	}

	public Version getDistributionVersion() {
		return ((VersionValue) get(GatewayStateName.DISTRIBUTION_VERSION)).getVersion();
	}

	@Override
	public JSONObject toJson() {
		JSONObject gatewayJson = super.toJson();
// TODO finish asynchrounously call... since we need to refactor dimension controller, I don't finish now.
//		final boolean dateReady = false;
//		final Date date;
//		final String dateValue;
//		getDate(new DimensionStatusListener<Date>() {
//			@Override
//			public void onDimensionStatus(Date status, CommandResult result) {
//				date = status;
//				dateReady = true;
//				
//			}
//		});
//		final boolean dateTimeReady = false;
//		final DateTime dateTime;
//		final String dateTimeValue;
//		getDateTime(new DimensionStatusListener<DateTime>() {
//			@Override
//			public void onDimensionStatus(DateTime status, CommandResult result) {
//				dateTime = status;
//				dateTimeReady = true;
//				
//			}
//		});
//		final boolean timeReady = false;
//		final Time time;
//		final String timeValue;
//		getTime(new DimensionStatusListener<Time>() {
//			@Override
//			public void onDimensionStatus(Time status, CommandResult result) {
//				time = status;
//				timeReady = true;
//				
//			}
//		});
//		final boolean upTimeReady = false;
//		final Time upTime;
//		final String upTimeValue;
//		getTime(new DimensionStatusListener<Time>() {
//			@Override
//			public void onDimensionStatus(Time status, CommandResult result) {
//				upTime = status;
//				upTimeReady = true;
//				
//			}
//		});
//		long startTime = System.currentTimeMillis();
//		long currentTime = startTime;
//		while ((!dateReady || !dateTimeReady || !timeReady || !upTimeReady)
//			&& ((currentTime-startTime) > 4000)) { // 4s timeout
//			// wait
//			wait(100);
//			currentTime = System.currentTimeMillis();
//		}
//		if (date != null) {
//			dateValue = new SimpleDateFormat("dd-MM-yyyy").format(date.getDate());
//		}
//		if (dateTime != null) {
//			dateTimeValue = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(dateTime.getDate());
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//		if (time != null) {
//			timeValue = sdf.format(time.getTime());
//		}
//		if (upTime != null) {
//			upTimeValue = sdf.format(upTime.getTime());
//		}

//		gatewayJson.put("where", getWhere())
//				 .put("what", "NONE")
//				 .put("date", dateValue)
//				 .put("datetime",dateTimeValue)
//				 .put("deviceType", getDeviceType())
//				 .put("distributionVersion", getDistributionVersion().toString())
//				 .put("firmwareVersion", getFirmwareVersion().toString())
//				 .put("ipAddress", getIpAddress())
//				 .put("kernelVersion", getKernelVersion().toString())
//				 .put("netMask", getNetMask());
//				 .put("time",timeValue)
//				 .put("upTime",upTimeValue);
		return gatewayJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		super.fromJson(jsonObject);
//		setWhere(jsonObject.getString("where"));
	}
}
