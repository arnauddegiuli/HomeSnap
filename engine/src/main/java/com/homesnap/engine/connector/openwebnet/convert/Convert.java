package com.homesnap.engine.connector.openwebnet.convert;

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
	 * @return open web net message.
	 */
	protected final static String createMessage(Command command) {
		if (command.getWhere() == null && command.getWhere().getTo() == null) {
			throw new IllegalArgumentException("Controller must contain an address with where");
		}
		
		String who = OpenWebNetWho.convert(command.getWho());
		String where = command.getWhere().getTo();
		if (command.isActionCommand()) {
			if (StateName.STATUS.equals(command.getWhat().getName())) {
				return MessageFormat.format(OpenWebNetConstant.COMMAND, new Object[] {who, convertStatus(command.getWho(), command.getWhat().getValue()), where});
			} else { // Dimension
				DimensionStatus dimensionStatus = convert(command.getWho(), command.getWhat()); 
				StringBuilder sb = new StringBuilder();
				for (DimensionValue dimension : dimensionStatus.getValueList()) {
					sb.append(dimension.getValue());
					sb.append(OpenWebNetConstant.DIMENSION_SEPARATOR);
				}
				sb.setLength(sb.length()-1);
				
				return MessageFormat.format(OpenWebNetConstant.DIMENSION_COMMAND, new Object[] {who, where, dimensionStatus.getCode(), sb.toString()});
			}
		} else {
			if (StateName.STATUS.equals(command.getWhat().getName())) {
				return MessageFormat.format(OpenWebNetConstant.STATUS, new Object[] {who, where});
			} else {
				DimensionStatus dimensionStatus = convert(command.getWho(), command.getWhat());
				return MessageFormat.format(OpenWebNetConstant.DIMENSION_STATUS, new Object[] {who, where, dimensionStatus.getCode()});
			}
		}
	}
	
//	public Command createCommand(String message) {
//	TODO plus tard mettre l'inverse ici (actuellement dans monitor)	
//	}
	
	
	private static DimensionStatus convert(Who w, State state) {
		DimensionStatus ds = null;
		switch (w) {
		case AUTOMATION:
			break;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			break;
		case GATEWAY:
			ds = GatewayDimension.fromValue(state.getName()).createDimensionStatus();
//			ds.setValueList(dimensionList); TODO manage values
			break;
		case HEATING_ADJUSTMENT:
			ds=HeatingZoneDimension.fromState(state);
			break;
		case LIGHT:
			break;
		case MULTIMEDIA:
			break;
		case POWER_MANAGEMENT:
			break;
		case SCENARIO:
			break;
		case SOUND_SYSTEM:
			break;
		default:
//			throw new UnknownState(); UnknownWho
		}
		
		return ds;
	}
	
	protected static State convert(Who w, String code, List<DimensionValue> dimensionList) throws UnknownState {
		DimensionStatus dimension;
		switch (w) {
		case AUTOMATION:
			throw new UnknownState();
//			break;
		case DIAGNOSTIC_OF_HEATING_ADJUSTMENT:
			throw new UnknownState();
//			break;
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
			throw new UnknownState();
//			break;
		case MULTIMEDIA:
			throw new UnknownState();
//			break;
		case POWER_MANAGEMENT:
			throw new UnknownState();
//			break;
		case SCENARIO:
			throw new UnknownState();
//			break;
		case SOUND_SYSTEM:
			throw new UnknownState();
//			break;
		default:
			throw new UnknownState();
		}
	}
	
	
	private static String convertStatus(Who who, StateValue stateValue) {
		switch (who) {
		case LIGHT:
			return LightStatus.fromValue(stateValue).getCode(); // TODO manage null
		case HEATING_ADJUSTMENT:
			return HeatingZoneStatus.fromValue(stateValue).getCode();
		default:
			return stateValue.getValue(); // TODO mapping
		}
			
		
	}
	
	protected static StateValue convertStatus(Who who, String code) {
		switch (who) {
		case LIGHT:
			return LightStatus.fromValue(code).getValue(); // TODO manage null
		case HEATING_ADJUSTMENT:
			return HeatingZoneStatus.fromValue(code).getValue();
		default:
			return null; // TODO mapping
		}
		
	}
}
