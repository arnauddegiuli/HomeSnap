package com.homesnap.engine.connector.openwebnet.convert;

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

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;
import com.homesnap.engine.connector.openwebnet.automation.AutomationStatusConverter;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimensionConverter;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneStatus;
import com.homesnap.engine.connector.openwebnet.light.LightStatusConverter;
import com.homesnap.engine.controller.light.LightStateName;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.who.Who;

public class Convert {

	/**
	 * Create the open message for action or status.
	 * 
	 * @return open web net message.
	 */
	protected final static List<String> createMessage(Command command) {
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
					return MessageFormat.format(
						OpenWebNetConstant.COMMAND,
						new Object[] {
								who,
								convertStatus(command.getWho(), what.getValue()),
								where 
						}
					);
				} else { // Dimension
					DimensionStatus<?> dimensionStatus = convertDimension(command.getWho(), what);
					StringBuilder sb = new StringBuilder();
					for (DimensionValue dimension : dimensionStatus.getValueList()) {
						sb.append(dimension.getValue());
						sb.append(OpenWebNetConstant.DIMENSION_SEPARATOR);
					}
					sb.setLength(sb.length() - 1);

					return MessageFormat.format(
							OpenWebNetConstant.DIMENSION_COMMAND,
							new Object[] { who, where, dimensionStatus.getCode(), sb.toString() }
					);
				}
			} else { // Statut request
				if (OpenWebNetCommand.DEFAULT_ACTION.equalsIgnoreCase(what.getName())) {
					return MessageFormat.format(OpenWebNetConstant.STATUS,
							new Object[] { who, where });
				} else {
					DimensionStatus<?> dimensionStatus = convertDimension(command.getWho(), what);
					return MessageFormat.format(
							OpenWebNetConstant.DIMENSION_STATUS, new Object[] {
									who, where, dimensionStatus.getCode() });
				}
			}
		} catch (UnknownState e) {
			throw new IllegalArgumentException("Controller status unknown [" + command.getWhat().getName() + "]");
		} catch (UnknownWho e) {
			throw new IllegalArgumentException("Controller Who unknown. [" + command.getWho() + "]");
		} catch (UnSupportedState e) {
//			TODO Log. ("Controller status unsupported. [" + command.getWhat().getName() + "]");
			return null;
		} catch (UnknownStateValue e) {
			throw new IllegalArgumentException("Controller status value unknown [" + command.getWhat().getValue() != null ? command.getWhat().getValue().toString() : "null" + "]");
		}
	}

	// public Command createCommand(String message) {
	// TODO plus tard mettre l'inverse ici (actuellement dans monitor)
	// }

	private static List<DimensionStatus<?>> convertDimension(Who w, What what, Map<String, State<?>> controllerStateList)
			throws UnknownState, UnknownWho, UnSupportedState {
		DimensionStatus<?> ds = null;
		switch (w) {
		case AUTOMATION:
			break;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			break;
		case GATEWAY:
			ds = GatewayDimensionConverter.convert(what);
			break;
		case HEATING_ADJUSTMENT:
			ds = HeatingZoneDimension.fromState(what);
			break;
		case LIGHT:
			if(LightStateName.COLOR.getName().equalsIgnoreCase(what.getName())){
				throw new UnSupportedState();
			} else if(LightStateName.BLINK_TIME.getName().equalsIgnoreCase(what.getName())){
				throw new UnSupportedState();
			} else if(LightStateName.LEVEL.getName().equalsIgnoreCase(what.getName())){
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

	private static List<What> convertDimension(Who w, String code,
			List<DimensionValue> dimensionList, Map<String, State<?>> controllerStateList) throws UnknownState, UnknownWho {
		DimensionStatus<?> dimension;
		switch (w) {
		case AUTOMATION:
			throw new UnknownState(); // Only status manage by automation: no dimension
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			throw new UnknownWho();
			// break;
		case GATEWAY:
			return GatewayDimensionConverter.convert(code, dimensionList);
		case HEATING_ADJUSTMENT:
			HeatingZoneDimension hd = HeatingZoneDimension.fromValue(code);
			dimension = hd.createDimensionStatus();
			dimension.setValueList(dimensionList);
			return new State(hd.getName(), dimension.getStateValue());
		case LIGHT:
			throw new UnknownState(); // Only status manage by light: no dimension
		case MULTIMEDIA:
			throw new UnknownWho();
			// break;
		case POWER_MANAGEMENT:
			throw new UnknownWho();
			// break;
		case SCENARIO:
			throw new UnknownWho();
			// break;
		case SOUND_SYSTEM:
			throw new UnknownWho();
			// break;
		case ENERGY_MANAGEMENT:
			throw new UnknownWho();
		default:
			throw new UnknownWho();
		}
	}

	private static List<String> convertStatus(Who who, What what, Map<String, State<?>> controllerStateList)
			throws UnknownStateValue, UnknownWho {
		switch (who) {
		case LIGHT:
			String ls = LightStatusConverter.convert(what);
			if (ls == null) {
				throw new UnknownStateValue();
			} else {
				return ls;
			}
		case AUTOMATION:
			String as = AutomationStatusConverter.fromValue(what);
			if (as == null) {
				throw new UnknownStateValue();
			} else {
				return as;
			}
		case HEATING_ADJUSTMENT:
			return HeatingZoneStatus.fromValue(what).getCode();
		case ENERGY_MANAGEMENT:
		default:
			throw new UnknownWho();
		}
	}

	private static List<What> convertStatus(Who who, String code, Map<String, State<?>> controllerStateList)
			throws UnknownStateValue {
		switch (who) {
		case LIGHT:
			What ls = LightStatusConverter.convert(code);
			if (ls == null) {
				throw new UnknownStateValue();

			} else {
				return ls;
			}
		case AUTOMATION:
			What as = AutomationStatusConverter.fromValue(code);
			if (as == null) {
				throw new UnknownStateValue();

			} else {
				return as;
			}
		case HEATING_ADJUSTMENT:
			return HeatingZoneStatus.fromValue(code).getValue();
		case ENERGY_MANAGEMENT:
			
		default:
			return null;
		}

	}
}
