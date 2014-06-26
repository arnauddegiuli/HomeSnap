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

import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.openwebnet.OpenWebNetConstant;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.gateway.GatewayDimension;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneDimension;
import com.homesnap.engine.connector.openwebnet.heating.HeatingZoneStatus;
import com.homesnap.engine.connector.openwebnet.light.LightStatus;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.who.Who;

public class Convert {

	/**
	 * Create the open message for action or status.
	 * 
	 * @return open web net message.
	 */
	protected final static String createMessage(Command command) {
		if (command.getWhere() == null || command.getWhere().getTo() == null) {
			if (command.getWho() != Who.GATEWAY) {
				throw new IllegalArgumentException("Command must contain a where");
			}
		}

		try {
			String who = OpenWebNetWho.convert(command.getWho()).getValue();
			String where = command.getWhere().getTo();
			State what = command.getWhat();
			if (command.isActionCommand()) {
				if (StateName.STATUS.equals(what.getName())) {
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
			} else {
				if (StateName.STATUS.equals(what.getName())) {
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
			throw new IllegalArgumentException("Controller status unsupported [" + command.getWhat().getName() + "]");
		} catch (UnknownWho e) {
			throw new IllegalArgumentException("Controller Who unsupported. [" + command.getWho() + "]");
		}
	}

	// public Command createCommand(String message) {
	// TODO plus tard mettre l'inverse ici (actuellement dans monitor)
	// }

	private static DimensionStatus<?> convertDimension(Who w, State state)
			throws UnknownState, UnknownWho {
		DimensionStatus<?> ds = null;
		switch (w) {
		case AUTOMATION:
			break;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			break;
		case GATEWAY:
			ds = GatewayDimension.fromValue(state.getName())
					.createDimensionStatus();
			// ds.setValueList(dimensionList); TODO manage values
			break;
		case HEATING_ADJUSTMENT:
			ds = HeatingZoneDimension.fromState(state);
			break;
		case LIGHT:
			throw new UnknownState(); // Only status manage by light: no dimension
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

	protected static State convertDimension(Who w, String code,
			List<DimensionValue> dimensionList) throws UnknownState, UnknownWho {
		DimensionStatus<?> dimension;
		switch (w) {
		case AUTOMATION:
			throw new UnknownWho();
			// break;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			throw new UnknownWho();
			// break;
		case GATEWAY:
			GatewayDimension gd = GatewayDimension.fromValue(code);
			dimension = gd.createDimensionStatus();
			dimension.setValueList(dimensionList);
			return new State(gd.getName(), dimension.getStateValue());
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

	private static String convertStatus(Who who, StateValue stateValue)
			throws UnknownState, UnknownWho {
		switch (who) {
		case LIGHT:
			LightStatus ls = LightStatus.fromValue(stateValue);
			if (ls == null) {
				throw new UnknownState();
			} else {
				return ls.getCode();
			}
		case HEATING_ADJUSTMENT:
			return HeatingZoneStatus.fromValue(stateValue).getCode();
		case ENERGY_MANAGEMENT:
		default:
			throw new UnknownWho();
		}
	}

	protected static StateValue convertStatus(Who who, String code)
			throws UnknownState {
		switch (who) {
		case LIGHT:
			LightStatus ls = LightStatus.fromValue(code);
			if (ls == null) {
				throw new UnknownState();

			} else {
				return ls.getValue();
			}
		case HEATING_ADJUSTMENT:
			return HeatingZoneStatus.fromValue(code).getValue();
		case ENERGY_MANAGEMENT:
			
		default:
			return null;
		}

	}
}
