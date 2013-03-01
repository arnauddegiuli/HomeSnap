package com.adgsoftware.mydomo.engine.controller.gateway;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.DimensionStatusListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Date;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.DateTime;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.DistributionVersion;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.FirmwareVersion;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.IpAddress;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.KernelVersion;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Model;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.NetMask;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.Time;
import com.adgsoftware.mydomo.engine.controller.gateway.dimension.UpTime;

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
//		VERSION(); TODO manage version
		
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
		return Command.WHO_GATEWAY;
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
	
}
