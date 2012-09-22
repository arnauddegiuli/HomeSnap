package com.adgsoftware.mydomo.test;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.OpenWebCommander;
import com.adgsoftware.mydomo.engine.connector.OpenWebMonitor;
import com.adgsoftware.mydomo.engine.connector.impl.OpenWebCommanderImpl;
import com.adgsoftware.mydomo.engine.connector.impl.OpenWebMonitorImpl;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.test.Constants.Light1;

public class CommandSessionClient {
	
	public static void main(String[] args) {
		OpenWebCommander server = new OpenWebCommanderImpl("localhost", 1234, 0L);
		OpenWebMonitor monitor = new OpenWebMonitorImpl("localhost", 1235, 0L);
		server.connect();
		monitor.connect();
		
		Light l = new Light();
		Gateway g = new Gateway();
		
		server.addControllerToExecute(l);
		monitor.addControllerToMonitor(l);
	
		server.addControllerToExecute(g);
		monitor.addControllerToMonitor(g);
		
		g.setWhere(Constants.Gateway.GATEWAY_ADDRESS);
		g.getFirmwareVersion();
		g.getDate();
		g.getDateTime(); // TODO pb with hours
		g.getDeviceType();
		g.getDistributionVersion();
		g.getIpAddress();
		g.getKernelVersion();
		g.getNetMask();
		g.getTime();
		g.getUpTime();
		
		
		l.addControllerChangeListener(new CommandSessionClient.Listener());
		l.setWhere(Light1.LIGHT_ADDRESS);
		l.setWhat(Light1.LIGHT_ON);
	}
	
	static class Listener implements com.adgsoftware.mydomo.engine.controller.ControllerChangeListener {

		/**  */
		private static final long serialVersionUID = 1L;

		@Override
		public void onWhatChange(Controller<? extends Status> controller,
				Status oldStatus, Status newStatus) {
			System.out.println("Device change [where:" + controller.getWhere() + ";what:"+controller.getWhat() + "]");
		}

		@Override
		public void onWhatChangeError(Controller<? extends Status> controller,
				Status oldStatus, Status newStatus, CommandResult result) {
			// TODO Auto-generated method stub
			
		}

	}
}
