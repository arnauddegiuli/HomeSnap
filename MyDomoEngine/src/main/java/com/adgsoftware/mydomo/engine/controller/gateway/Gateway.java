package com.adgsoftware.mydomo.engine.controller.gateway;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
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
		super.setWhere(newValue);
		if (newValue == null) { // Manage null value because we create the controller with no address
			// TODO reset dimnesion!
		} else {
			changeDimensionStatus(getDimensionStatus(Time.class));
			changeDimensionStatus(getDimensionStatus(Date.class));
		}
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
	
	public java.util.Date getTime() {
		Time t = (Time)getDimensionStatusFromCache(Gateway.GatewayDimension.TIME.getCode());
		if (t == null) {
			return null;
		}
		return t.getTime();
	}
	
	public void setTime(java.util.Date date) {
		Time time = new Time();
		time.setTime(date);
		setDimensionStatus(time);
	}
	
	public java.util.Date getDate() {
		Date d = (Date)getDimensionStatusFromCache(Gateway.GatewayDimension.DATE.getCode());
		if (d == null) {
			return null;
		}
		return d.getDate();
	}
	
	public void setDate(java.util.Date newDate) {
		Date date = new Date();
		date.setTime(newDate);
		setDimensionStatus(date);
	}
	
	public byte[] getIpAddress() {
		IpAddress ip = getDimensionStatus(IpAddress.class);
		if (ip == null)
			return null;
		return ip.getIpAddress();
	}
	
	public byte[] getNetMask() {
		NetMask netmask = getDimensionStatus(NetMask.class);
		if (netmask == null)
			return null;
		return netmask.getNetMask();
	}
	
	public String getDeviceType() {
		Model model = getDimensionStatus(Model.class);
		if (model == null)
			return null;
		return model.getModel();
	}
	
	public Version getFirmwareVersion() {
		FirmwareVersion v = getDimensionStatus(FirmwareVersion.class);
		if (v == null) {
			return null;
		}
		return v.getVersion();
	}
	
	public java.util.Date getUpTime() {
		UpTime upTime = getDimensionStatus(UpTime.class);
		if (upTime == null)
			return null;
		
		return upTime.getTime();
	}
	
	public java.util.Date getDateTime() {
		DateTime dt = getDimensionStatus(DateTime.class);
		if (dt == null)
			return null;
		return dt.getDate();
	}
	
	public void setDateTime(java.util.Date date) {
		DateTime dt = new DateTime();
		dt.setTime(date);
		setDimensionStatus(dt);
	}
	
	public Version getKernelVersion() {
		KernelVersion v = getDimensionStatus(KernelVersion.class);
		if (v == null)
			return null;
		return v.getVersion();
	}
	
	public Version getDistributionVersion() {
		DistributionVersion v = getDimensionStatus(DistributionVersion.class);
		if (v == null)
			return null;
		return v.getVersion();
	}
	
}
