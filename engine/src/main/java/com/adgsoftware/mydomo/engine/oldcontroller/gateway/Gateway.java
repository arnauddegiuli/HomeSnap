package com.adgsoftware.mydomo.engine.oldcontroller.gateway;

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


import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.oldconnector.CommandResult;
import com.adgsoftware.mydomo.engine.oldconnector.ControllerType;
import com.adgsoftware.mydomo.engine.oldcontroller.ControllerDimension;
import com.adgsoftware.mydomo.engine.oldcontroller.DimensionStatusListener;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.Date;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.DateTime;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.DistributionVersion;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.FirmwareVersion;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.IpAddress;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.KernelVersion;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.Model;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.NetMask;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.Time;
import com.adgsoftware.mydomo.engine.oldcontroller.gateway.dimension.UpTime;

public class Gateway extends ControllerDimension<Gateway.GatewayStatus> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	public enum GatewayDimension {
		DATE("1"),
		DATETIME("22"),
		DISTRIBUTION_VERSION("24"),
		FIRMWARE_VERSION("16"),
		IP_ADDRESS("10"),
		KERNEL_VERSION("23"),
		MAC_ADDRESS("12"),
		MODEL("15"),
		NETMASK("11"),
		TIME("0"),
		UPTIME("19");
		
		private String code = "";
		private GatewayDimension(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
		
		public static GatewayDimension fromValue(String code) {
			for (GatewayDimension gd : GatewayDimension.values()) {
				if (gd.getCode().equals(code))
					return gd;
			}
			
			return null;
		}
	}
	
	public Gateway() {

	}
	
	/**
	 * Define the address of the device to control
	 * @param newValue address of the device
	 */
	public void setWhere(String newValue) {
		super.setWhere(""); // Gateway has no address since we are directly connected to it!
		// IpAddress
		getDimensionStatus(IpAddress.class, new DimensionStatusListener<IpAddress>() {

			@Override
			public void onDimensionStatus(IpAddress status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
		// NetMask
		getDimensionStatus(NetMask.class, new DimensionStatusListener<NetMask>() {

			@Override
			public void onDimensionStatus(NetMask status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
		// Model
		getDimensionStatus(Model.class, new DimensionStatusListener<Model>() {

			@Override
			public void onDimensionStatus(Model status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
		// Version
		getDimensionStatus(FirmwareVersion.class, new DimensionStatusListener<FirmwareVersion>() {

			@Override
			public void onDimensionStatus(FirmwareVersion status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
		// KernelVersion
		getDimensionStatus(KernelVersion.class, new DimensionStatusListener<KernelVersion>() {

			@Override
			public void onDimensionStatus(KernelVersion status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
		// DistributionVersion
		getDimensionStatus(DistributionVersion.class, new DimensionStatusListener<DistributionVersion>() {

			@Override
			public void onDimensionStatus(DistributionVersion status, CommandResult result) {
				changeDimensionStatus(status);
			}
		});
	}
	
	// GATEWAY
	public enum GatewayStatus implements Status {
		NONE;

		@Override
		public String getCode() {
			return null;
		}
	}
	
	@Override
	public String getWho() {
		return ControllerType.WHO_GATEWAY;
	}

	@Override
	public GatewayStatus getStatus(String code) {
		if (code == null) 
			return null;
		for (GatewayStatus status : GatewayStatus.values()) {
			if (code.equals(status.getCode())) {
				return status;
			}
		}	
		return null;
	}
	
	public void getTime(DimensionStatusListener<Time> listener) {
		getDimensionStatus(Time.class, listener);
	}
	
	public void setTime(java.util.Date date) {
		Time time = new Time();
		time.setTime(date);
		setDimensionStatus(time);
	}
	
	public void getDate(DimensionStatusListener<Date> listener) {
		getDimensionStatus(Date.class, listener);
	}
	
	public void setDate(java.util.Date newDate) {
		Date date = new Date();
		date.setTime(newDate);
		setDimensionStatus(date);
	}
	
	public byte[] getIpAddress() {
		IpAddress ip = (IpAddress)getDimensionStatusFromCache(Gateway.GatewayDimension.IP_ADDRESS.getCode());
		if (ip == null) {
			return null;
		}
		return ip.getIpAddress();
			
	}
	
	public byte[] getNetMask() {
		NetMask netmask = (NetMask)getDimensionStatusFromCache(Gateway.GatewayDimension.NETMASK.getCode());
		if (netmask == null)
			return null;
		return netmask.getNetMask();
	}
	
	public String getDeviceType() {
		Model model = (Model)getDimensionStatusFromCache(Gateway.GatewayDimension.MODEL.getCode());
		if (model == null)
			return null;
		return model.getModel();
	}
	
	public Version getFirmwareVersion() {
		FirmwareVersion v = (FirmwareVersion)getDimensionStatusFromCache(Gateway.GatewayDimension.FIRMWARE_VERSION.getCode());
		if (v == null) {
			return null;
		}
		return v.getVersion();
	}
	
	public void getUpTime(DimensionStatusListener<UpTime> listener) {
		getDimensionStatus(UpTime.class, listener);
	}
	
	public void getDateTime(DimensionStatusListener<DateTime> listener) {
		getDimensionStatus(DateTime.class, listener);
	}
	
	public void setDateTime(java.util.Date date) {
		DateTime dt = new DateTime();
		dt.setTime(date);
		setDimensionStatus(dt);
	}
	
	public Version getKernelVersion() {
		KernelVersion v = (KernelVersion)getDimensionStatusFromCache(Gateway.GatewayDimension.KERNEL_VERSION.getCode());
		if (v == null)
			return null;
		return v.getVersion();
	}
	
	public Version getDistributionVersion() {
		DistributionVersion v = (DistributionVersion)getDimensionStatusFromCache(Gateway.GatewayDimension.DISTRIBUTION_VERSION.getCode());
		if (v == null)
			return null;
		return v.getVersion();
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

		gatewayJson.put("where", getWhere())
				 .put("what", "NONE")
//				 .put("date", dateValue)
//				 .put("datetime",dateTimeValue)
				 .put("deviceType", getDeviceType())
				 .put("distributionVersion", getDistributionVersion().toString())
				 .put("firmwareVersion", getFirmwareVersion().toString())
				 .put("ipAddress", getIpAddress())
				 .put("kernelVersion", getKernelVersion().toString())
				 .put("netMask", getNetMask());
//				 .put("time",timeValue)
//				 .put("upTime",upTimeValue);
		return gatewayJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		super.fromJson(jsonObject);
		setWhere(jsonObject.getString("where"));
	}
}
