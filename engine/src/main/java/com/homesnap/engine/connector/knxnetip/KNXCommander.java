package com.homesnap.engine.connector.knxnetip;

import java.net.InetAddress;
import java.net.UnknownHostException;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.ConnectionListener;
import com.homesnap.engine.connector.knxnetip.network.NetworkConfig;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXCommander implements Commander {

	/** */
	private String ipAddress;

	/** */
	private KNXNetworkLink connection;
	
	/** */
	private ProcessCommunicator communicator;
	
	/**
	 * 
	 * @param ipAddress
	 */
	public KNXCommander(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public synchronized boolean connect() {
		if (!isConnected()) {

			// Get the local address which can bind the KNX net/IP device found during discovery
			KNXnetIPConnectionSettings knxConnectionSettings = KNXDiscoverer.getInstance().findKNXNetIPAddresses()
					.get(NetworkConfig.getInstance().getInetAdress(ipAddress));

//			if (knxConnectionSettings == null) {
//				throw new RuntimeException("No bindable KNXnetIP device found on the local network "+ ipAddress);
//			}
			try {
				knxConnectionSettings = new KNXnetIPConnectionSettings(InetAddress.getByName("192.168.0.50"), null, 0, TPSettings.TP1);
				
				connection = KNXConnection.createConnection(KNXNetworkLinkIP.ROUTER, knxConnectionSettings);
				communicator = new ProcessCommunicatorImpl(connection);
				System.out.println("Commander connected !");
			} catch (KNXException e) {
				closeCommunicator();
				System.out.println("Commander not connected !");
				e.printStackTrace();
			} catch (UnknownHostException e) {
				System.out.println("Commander not connected !");
				e.printStackTrace();
			}
		}
		return isConnected();
	}

	@Override
	public synchronized boolean isConnected() {
		return (connection != null) && connection.isOpen();
	}

	@Override
	public void disconnect() {
		if (isConnected()) {
			closeCommunicator();
		}
	}

	@Override
	public void addControllerToExecute(Controller controller) {
		controller.setServer(this);
	}

	@Override
	public void removeControllerToExecute(Controller controller) {
		controller.setServer(null);
	}

	@Override
	public void addConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sendCommand(final Command command, CommandListener resultListener) {
		CommandResult commandResult = null;
		if (!isConnected()) {
			connect();
		}
		if (isConnected()) {
			String targetAddress = command.getWhere().getTo();
			DPT dataPointType = KNXUtil.getStateType(command.getWho());
			if (dataPointType == null) {
				commandResult = new CommandResult() {
					@Override
					public Who getWho() { return command.getWho(); }
					@Override
					public Where getWhere() { return command.getWhere(); }
					@Override
					public State getWhat(StateName name) { return command.getWhat(); }
					@Override
					public CommandResultStatus getStatus() { return CommandResultStatus.error; }
				};
			} else {
				try {
					Datapoint datapoint = new StateDP(new GroupAddress(targetAddress), "", 0, dataPointType.getID());
					if (command.isStatusCommand()) { // Read
						final String stateValue = communicator.read(datapoint);
						commandResult = new CommandResult() {
							@Override
							public Who getWho() { return command.getWho(); }
							@Override
							public Where getWhere() { return command.getWhere(); }
							@Override
							public State getWhat(StateName name) { return new State(command.getWhat().getName(), KNXUtil.getStateValueToWrite(command.getWho(), stateValue)); }
							@Override
							public CommandResultStatus getStatus() { return CommandResultStatus.ok; }
						};
					} else if (command.isActionCommand()) {
						String stateValue = KNXUtil.getStateValueToRead(command.getWho(), command.getWhat().getValue());
						communicator.write(datapoint, stateValue);
						commandResult = new CommandResult() {
							@Override
							public Who getWho() { return command.getWho(); }
							@Override
							public Where getWhere() { return command.getWhere(); }
							@Override
							public State getWhat(StateName name) { return command.getWhat(); }
							@Override
							public CommandResultStatus getStatus() { return CommandResultStatus.ok; }
						};
					} else {
						commandResult = createCommandResult(command, CommandResultStatus.nok);
					}
				} catch (KNXException e) {
					commandResult = createCommandResult(command, CommandResultStatus.error);
				}
			}
		} else {
			commandResult = createCommandResult(command, CommandResultStatus.nok);
		}
		resultListener.onCommand(commandResult);
	}
	
	/**
	 * 
	 * @param command
	 * @param commandStatus
	 * @return
	 */
	private CommandResult createCommandResult(final Command command, final CommandResultStatus commandStatus) {
		return new CommandResult() {
			@Override
			public Who getWho() { return command.getWho(); }
			@Override
			public Where getWhere() { return command.getWhere(); }
			@Override
			public State getWhat(StateName name) { return command.getWhat(); }
			@Override
			public CommandResultStatus getStatus() { return commandStatus; }
		};		
	}

	/**
	 * 
	 */
	private void closeCommunicator() {
		try {
			if (communicator != null) {
				communicator.detach().close();
			}
		} finally {
			communicator = null;
			connection = null;
		}		
	}
}
