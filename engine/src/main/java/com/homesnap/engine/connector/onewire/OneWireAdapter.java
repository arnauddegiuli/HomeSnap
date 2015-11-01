package com.homesnap.engine.connector.onewire;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.dalsemi.onewire.OneWireAccessProvider;
import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.container.ADContainer;
import com.dalsemi.onewire.container.ClockContainer;
import com.dalsemi.onewire.container.HumidityContainer;
import com.dalsemi.onewire.container.MissionContainer;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.PotentiometerContainer;
import com.dalsemi.onewire.container.SwitchContainer;
import com.dalsemi.onewire.container.TemperatureContainer;
import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.ConnectionListener;
import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.connector.UnknownControllerListener;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.clock.ClockSensorStateName;
import com.homesnap.engine.controller.temperature.TemperatureSensorStateName;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.impl.DateState;
import com.homesnap.engine.controller.what.impl.DoubleState;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
class OneWireAdapter implements Monitor, Commander {
	
	/** */
	private DSPortAdapter adapter;
	
	/** */
	private OneWireMonitor monitor;
	
	/** */
	private Set<Controller> monitoredControllers = new HashSet<Controller>();
	
	/**
	 * 
	 */
	public OneWireAdapter() {
	}
	
//	@Override
	public String getName() {
		return "1-Wire adapter";
	}

	@Override
	public synchronized boolean connect() {
		if (!isConnected()) { 
			try {
				adapter = OneWireAccessProvider.getDefaultAdapter(); // TODO Detect by config
				monitor = new OneWireMonitor(adapter);
				monitor.start();
				synchronized (monitor) {
					try {
						// Let the monitor detecting devices for the first time
						monitor.wait(500);
					} catch (InterruptedException e) {
					}
				}
			} catch (OneWireException e) {
				e.printStackTrace(); // TODO Log
			}
		}
		return isConnected();
	}

	@Override
	public synchronized void disconnect() {
		if (isConnected()) {
			monitor.stop();
			try {
				adapter.freePort();
			} catch (OneWireException e) {
			} finally {
				adapter = null;
			}
//			monitoredControllers.clear(); TODO If we don't clean known controllers list, we have to reconciliate them when reconnecting
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
	public void addControllerToMonitor(Controller controller) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeControllerToMonitor(Controller controller) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void addConnectionListener(ConnectionListener connectionListener) {
		// TODO ???
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		// TODO ???
	}

	@Override
	public void addControllerStatusListener(CommandListener commandListener) {
		// TODO ???
	}

	@Override
	public void removeControllerStatusListener(CommandListener commandListener) {
		// TODO ???
	}

	@Override
	public void addUnknownControllerListener(UnknownControllerListener unknownControllerListener) {
		// TODO ???
	}

	@Override
	public void removeUnknownControllerListener(UnknownControllerListener unknownControllerListener) {
		// TODO ???
	}

	@Override
	public boolean isConnected() {
		return (adapter != null); // TODO detected status of the adapter
	}

	@Override
	public void sendCommand(final Command command, CommandListener resultListener) {
		if (isConnected()) {
			OneWireContainer owc = monitor.getContainer(command.getWhere().getTo());
			if (owc == null) {
				resultListener.onCommand(new CommandResult() {
					
					@Override
					public CommandResultStatus getStatus() {
						return CommandResultStatus.nok;
					}

					@Override
					public State getWhat(StateName name) {
						return null;
					}

					@Override
					public Who getWho() {
						return null;
					}

					@Override
					public Where getWhere() {
						return null;
					}
				});
			} else {
				executeCommand(owc, command);
				resultListener.onCommand(new CommandResult() {
					
					@Override
					public CommandResultStatus getStatus() {
						return CommandResultStatus.ok;
					}

					@Override
					public State getWhat(StateName name) {
						return null;
					}

					@Override
					public Who getWho() {
						return null;
					}

					@Override
					public Where getWhere() {
						return null;
					}
				});
			}
		} else {
			System.out.println(Thread.currentThread().getName() +" not connected !");
			resultListener.onCommand(new CommandResult() {
				
				@Override
				public CommandResultStatus getStatus() {
					return CommandResultStatus.error;
				}

				@Override
				public State getWhat(StateName name) {
					return null;
				}

				@Override
				public Who getWho() {
					return null;
				}

				@Override
				public Where getWhere() {
					return null;
				}
			});
		}
	}

	private void executeCommand(OneWireContainer container, Command command) {
		
		// OneWireSensor
		if (container instanceof ADContainer) {
		}
		if (container instanceof ClockContainer) {
			sendClockCommand(command, (ClockContainer) container);
		}
		if (container instanceof HumidityContainer) {
		}
		if (container instanceof MissionContainer) {
		}
		if (container instanceof PotentiometerContainer) {
		}
		if (container instanceof TemperatureContainer) {
			sendTemperatureCommand(command, (TemperatureContainer) container);
		}
		if (container instanceof SwitchContainer) {
		}
	}
	
	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
	private static final String DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	
	private void sendClockCommand(Command command, ClockContainer container) {
		
		new OneWireSensorCommand<ClockContainer>(adapter, command, container) {

			@Override
			public StateValue read(State state, ClockContainer sensor, byte[] deviceState) throws OneWireException {
				
				StateValue result = null;
				if (state.getName() instanceof ClockSensorStateName) {
					
					ClockSensorStateName stateName = (ClockSensorStateName) state.getName();
					switch (stateName) {
						case ALARM: {
							// TODO Faire un DateUtil
							long time = sensor.getClockAlarm(deviceState);
							result = new DateState(new Date(time));
							break;
						}
						
						case DATE:
						case TIME:
						case DATE_TIME: {
//							String pattern;
//							if (StateNameEnum.DATE.equals(state.getName())) {
//								pattern = DEFAULT_DATE_PATTERN;
//							}
//							if (StateNameEnum.TIME.equals(state.getName())) {
//								pattern = DEFAULT_TIME_PATTERN;
//							}
//							if (StateNameEnum.DATE_TIME.equals(state.getName())) {
//								pattern = DEFAULT_DATE_TIME_PATTERN;
//							}
//							else {
//								throw new IllegalArgumentException("StateName is not supported : "+ state.getName());
//							}
							
							long time = sensor.getClock(deviceState);
							result = new DateState(new Date(time));
//							result = new StringValue(new SimpleDateFormat(pattern).format(new Date(time)));
							break;
						}
						default: {
							result = null;
						}
					}
				}
				return result;
			}

			@Override
			public void write(State state, ClockContainer sensor, byte[] deviceState) throws OneWireException {
				
				if (state.getName() instanceof ClockSensorStateName) {
					
					ClockSensorStateName stateName = (ClockSensorStateName) state.getName();
					switch (stateName) {
						case ALARM: {
							Date newDate;
							try {
								newDate = new SimpleDateFormat(DEFAULT_TIME_PATTERN).parse(state.getValue().getValue());
								sensor.setClockAlarm(newDate.getTime(), deviceState);
							} catch (ParseException e) {
								// TODO
								e.printStackTrace();
							}
							break;
						}
						
						case DATE:
						case TIME:
						case DATE_TIME: {
							Date newDate;
							try {
								newDate = new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN).parse(state.getValue().getValue());
								sensor.setClock(newDate.getTime(), deviceState);
							} catch (ParseException e) {
								// TODO
								e.printStackTrace();
							}
							break;
						}
						default: {
						}
					}
				}
			}
			
		}.send();
	}

	private void sendTemperatureCommand(Command command, TemperatureContainer container) {
		
		new OneWireSensorCommand<TemperatureContainer>(adapter, command, container) {
			
			@Override
			public StateValue read(State state, TemperatureContainer sensor, byte[] deviceState) throws OneWireException {
				
				StateValue result = null;
				if (state.getName() instanceof TemperatureSensorStateName) {
					
					TemperatureSensorStateName stateName = (TemperatureSensorStateName) state.getName();
					switch (stateName) {
						case ALARM_HIGH: {
							result = new DoubleState(sensor.getTemperatureAlarm(TemperatureContainer.ALARM_HIGH, deviceState));
							break;
						}
						case ALARM_LOW: {
							result = new DoubleState(sensor.getTemperatureAlarm(TemperatureContainer.ALARM_LOW, deviceState));
							break;
						}
						case HIGHEST_TEMP: {
							result = new DoubleState(sensor.getMaxTemperature());
							break;
						}
						case LOWEST_TEMP: {
							result = new DoubleState(sensor.getMinTemperature());
							break;
						}
						default: {
							result = null;
						}
					}
				}
				return result;
			}
			
			@Override
			public void write(State state, TemperatureContainer sensor, byte[] deviceState) throws OneWireException {
				
				if (state.getName() instanceof TemperatureSensorStateName) {
					
					TemperatureSensorStateName stateName = (TemperatureSensorStateName) state.getName();
					switch (stateName) {
						case ALARM_HIGH: {
							sensor.setTemperatureAlarm(TemperatureContainer.ALARM_HIGH,
									((DoubleState) state.getValue()).getDoubleValue(), deviceState);
							break;
						}
						case ALARM_LOW: {
							sensor.setTemperatureAlarm(TemperatureContainer.ALARM_LOW,
									((DoubleState) state.getValue()).getDoubleValue(), deviceState);
							break;
						}
						default: {
							// TODO log.debug()
						}
					}
				}
			}
		}.send();
	}
}
