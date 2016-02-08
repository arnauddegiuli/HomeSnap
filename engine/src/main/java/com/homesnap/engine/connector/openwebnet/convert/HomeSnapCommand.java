package com.homesnap.engine.connector.openwebnet.convert;

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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.light.LightStatusConverter;
import com.homesnap.engine.controller.light.LightStateName;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.who.Who;

public class HomeSnapCommand {
	
	private Log log = new Log();
	
	// TODO Improve way to differenciate single action from dimension action commands
	public static String DEFAULT_ACTION = "status";

	private List<String> command = new ArrayList<String>();

	public HomeSnapCommand(Command command) {
		this.command = createMessage(command);
	}


	public List<String> getCommand() {
		return command;
	}
	
	
	/**
	 * Create the open message for action or status.
	 * 
	 * @return open web net message.
	 */
	protected final List<String> createMessage(Command command) {
		if (command.getWhere() == null || command.getWhere().getTo() == null) {
			if (command.getWho() != Who.GATEWAY) {
				throw new IllegalArgumentException("Command must contain a where");
			}
		}

		try {
			String who = OpenWebNetWho.convert(command.getWho()).getValue();
			String where = command.getWhere().getTo();
			What what = command.getWhat();
			if (command.isActionCommand()) {
				if (OpenWebNetCommand.DEFAULT_ACTION.equalsIgnoreCase(what.getName())) {
					List<String> whatList = convertStatus(command.getWho(), what, command.getSource().getStateMap());
					List<String> commandList  = new ArrayList<String>();
					for (String string : whatList) {
						commandList.add(MessageFormat.format(
								OpenWebNetConstant.COMMAND,
								new Object[] {
										who,
										string,
										where 
								}));
					}
					return commandList;
				} else { // Dimension
					List<DimensionStatus<?>> dimensionStatus = convertDimension(command.getWho(), what, command.getSource().getStateMap());
					List<String> commandList  = new ArrayList<String>();
					StringBuilder sb = new StringBuilder();
					
					for (DimensionStatus<?> dim : dimensionStatus) {
						for (DimensionValue dimension : dim.getValueList()) {
							sb.append(dimension.getValue());
							sb.append(OpenWebNetConstant.DIMENSION_SEPARATOR);
						}
						sb.setLength(sb.length() - 1);
						commandList.add(MessageFormat.format(
								OpenWebNetConstant.DIMENSION_COMMAND,
								new Object[] { who, where, dim.getCode(), sb.toString() }
						));
					}
					
					return commandList;
				}
			} else { // Statut request
				if (OpenWebNetCommand.DEFAULT_ACTION.equalsIgnoreCase(what.getName())) {
					List<String> commandList  = new ArrayList<String>();
					commandList.add(MessageFormat.format(OpenWebNetConstant.STATUS,
							new Object[] { who, where }));
					return commandList;
				} else {
					List<DimensionStatus<?>> dimensionStatus = convertDimension(command.getWho(), what, command.getSource().getStateMap());
					List<String> commandList  = new ArrayList<String>();
					
					for (DimensionStatus<?> dim : dimensionStatus) {
						commandList.add(MessageFormat.format(
								OpenWebNetConstant.DIMENSION_STATUS, new Object[] {
										who, where, dim.getCode() }));
					}
					return commandList;
				}
			}
		} catch (UnknownState e) {
			throw new IllegalArgumentException("Controller status unknown [" + command.getWhat().getName() + "]");
		} catch (UnknownWho e) {
			throw new IllegalArgumentException("Controller Who unknown. [" + command.getWho() + "]");
		} catch (UnSupportedState e) {
			log.fine(Session.Command,"Controller status unsupported. [" + command.getWhat().getName() + "]");
			return null;
		} catch (UnknownStateValue e) {
			throw new IllegalArgumentException("Controller status value unknown [" + command.getWhat().getValue() != null ? command.getWhat().getValue().toString() : "null" + "]");
		}
	}
	
	
	private List<String> convertStatus(Who who, What what, Map<String, State<?>> controllerStateList)
			throws UnknownStateValue, UnknownWho {
		switch (who) {
		case LIGHT:
			List<String> ls = LightStatusConverter.convert(what, controllerStateList);
			if (ls == null) {
				throw new UnknownStateValue();
			} else {
				return ls;
			}
		case AUTOMATION:
//			String as = AutomationStatusConverter.fromValue(what);
//			if (as == null) {
				throw new UnknownStateValue();
//			} else {
//				return as;
//			}
		case HEATING_ADJUSTMENT:
//			return HeatingZoneStatus.fromValue(what).getCode();
		case ENERGY_MANAGEMENT:
		default:
			throw new UnknownWho();
		}
	}
	
	private static List<DimensionStatus<?>> convertDimension(Who w, What what, Map<String, State<?>> controllerStateList)
			throws UnknownState, UnknownWho, UnSupportedState {
		List<DimensionStatus<?>> ds = null;
		switch (w) {
		case AUTOMATION:
			break;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			break;
		case GATEWAY:
//			ds = GatewayDimensionConverter.convert(what);
			break;
		case HEATING_ADJUSTMENT:
//			ds = HeatingZoneDimension.fromState(what);
			break;
		case LIGHT:
			if(LightStateName.color.name().equalsIgnoreCase(what.getName())){
				throw new UnSupportedState();
			} else if(LightStateName.blink_time.name().equalsIgnoreCase(what.getName())){
				throw new UnSupportedState();
			} else if(LightStateName.blink.name().equalsIgnoreCase(what.getName())){
				throw new UnSupportedState();
			} else if(LightStateName.level.name().equalsIgnoreCase(what.getName())){
				throw new UnSupportedState();
			} else {
				throw new UnknownState(); // Only status manage by light: no dimension
			}
		case MULTIMEDIA:
			break;
		case POWER_MANAGEMENT:
			break;
		case SCENARIO:
			break;
		case SOUND_SYSTEM:
			break;
		case ENERGY_MANAGEMENT:
			break;
		default:
			throw new UnknownWho();
		}
		return ds;
	}
}
