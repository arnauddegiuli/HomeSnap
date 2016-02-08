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

import java.util.List;
import java.util.Map;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.connector.openwebnet.CommandEnum;
import com.homesnap.engine.connector.openwebnet.WhereType;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionStatus;
import com.homesnap.engine.connector.openwebnet.dimension.DimensionValue;
import com.homesnap.engine.connector.openwebnet.light.LightStatusConverter;
import com.homesnap.engine.connector.openwebnet.parser.CommandParser;
import com.homesnap.engine.connector.openwebnet.parser.ParseException;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

public class OpenWebNetCommand {
	
	private Log log = new Log();
	// TODO Improve way to differenciate single action from dimension action commands
	public static String DEFAULT_ACTION = "status";

	private String command;
	private CommandParser parser;

	public OpenWebNetCommand(String command) throws ParseException {
		try {
			this.command = command;
			parser = CommandParser.parse(command);
		} catch (ParseException e) {
			System.out.println("Invalid command [" + command + "].");
			throw e;
		}
	}

	public boolean isStandardCommand() {
		return CommandEnum.STANDARD_COMMAND.equals(parser.getType());
	}

	public boolean isDimensionCommand() {
		return CommandEnum.DIMENSION_COMMAND.equals(parser.getType());
	}

	public boolean isGeneralCommand() {
		return WhereType.GENERAL.equals(parser.getWhereType());
	}

	public boolean isGroupCommand() {
		return WhereType.GROUP.equals(parser.getWhereType());
	}

	public boolean isEnvironmentCommand() {
		return WhereType.ENVIRONMENT.equals(parser.getWho());
	}

	public List<What> getWhat(Map<String, State<?>> controllerStateList) {
		try {
			return convertStatus(getWho(), parser.getWhat(), controllerStateList);
		} catch (UnknownStateValue e) {
			log.fine(Session.Monitor, "UnknowStateValue ["+ parser.getWhat() + "]");
			return null;
		}
	}

	public Who getWho() {
		return OpenWebNetWho.convert(parser.getWho());
	}

	public Where getWhere() {
		String where = parser.getWhere();
		return new Where(where, where);

	}

	public String getGroup() {
		return parser.getGroup();
	}

	public String getEnvironment() {
		return parser.getEnvironment();
	}

	public SpecialCommand getSpecialCommand() {
		return new SpecialCommand(parser);
	}

	public List<What> getDimension(Map<String, State<?>> controllerStateList) throws UnknownState, UnknownWho {
		return convertDimension(getWho(),  parser.getDimension(), parser.getDimensionList(), controllerStateList);
	}

	public String toString() {
		return command;
	}
	
	private static List<What> convertStatus(Who who, String code, Map<String, State<?>> controllerStateList)
			throws UnknownStateValue {
		switch (who) {
		case LIGHT:
			List<What> ls = LightStatusConverter.convert(code, controllerStateList);
			if (ls == null || ls.isEmpty()) {
				throw new UnknownStateValue();

			} else {
				return ls;
			}
		case AUTOMATION:
//			List<What> as = AutomationStatusConverter.fromValue(code);
//			if (as == null) {
				throw new UnknownStateValue();

//			} else {
//				return as;
//			}
		case HEATING_ADJUSTMENT:
//			return HeatingZoneStatus.fromValue(code).getValue();
		case ENERGY_MANAGEMENT:
			
		default:
			return null;
		}

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
//			return GatewayDimensionConverter.convert(code, dimensionList);
		case HEATING_ADJUSTMENT:
//			HeatingZoneDimension hd = HeatingZoneDimension.fromValue(code);
//			dimension = hd.createDimensionStatus();
//			dimension.setValueList(dimensionList);
//			return new State(hd.getName(), dimension.getStateValue());
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
}
