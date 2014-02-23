//package com.homesnap.engine.serialization;
//
///*
// * #%L
// * MyDomoEngine
// * %%
// * Copyright (C) 2011 - 2014 A. de Giuli
// * %%
// * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
// * 
// *     MyDomo is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     (at your option) any later version.
// * 
// *     MyDomo is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU General Public License for more details.
// * 
// *     You should have received a copy of the GNU General Public License
// *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
// * #L%
// */
//
//
//import java.io.Externalizable;
//import java.io.IOException;
//import java.io.ObjectInput;
//import java.io.ObjectOutput;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.json.JSONObject;
//
//import com.adgsoftware.mydomo.engine.controller.light.Light;
//import com.homesnap.engine.controller.light.LightController;
//import com.homesnap.engine.controller.light.LightStatus;
//import com.homesnap.engine.controller.state.StateName;
//import com.homesnap.engine.controller.state.StateValue;
//
///**
// * 
// * @author DRIESBACH Olivier
// * @version 1.0
// * @since 1.0
// */
//public class JSONLight extends Light implements Externalizable {
//
//	/**
//	 * 
//	 */
//	public JSONLight() {
//		super();
//	}
//
//	@Override
//	public void writeExternal(ObjectOutput out) throws IOException {
//		
//		JSONObject jsonController = new JSONObject();
//		jsonController.put("name", getName());
//		jsonController.put("description", getDescription());
//		jsonController.put("address", getAddress());
//		
//		JSONObject jsonStates = new JSONObject();
//		for(Entry<StateName, StateValue> state : getStates().entrySet()) {
//			jsonStates.put(state.getKey().getName(), state.getValue().getValue());
//		}
//		jsonController.put("states", jsonStates);
//		out.writeObject(jsonController.toString());
//	}
//
//	@Override
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//		
//		JSONObject jsonController = new JSONObject((String) in.readObject());
//		setName(jsonController.getString("name"));
//		setDescription(jsonController.getString("description"));
//		setAddress(jsonController.getString("address"));
//		
//		JSONObject jsonStates = jsonController.getJSONObject("states");
//		@SuppressWarnings("unchecked")
//		Set<String> states = (Set<String>) jsonStates.keySet();
//		for (String stateKey : states) {
//			
//			StateName state = StateName.fromValue(stateKey);
//			String value = jsonStates.getString(stateKey);
//			
//			switch (state) {
//				case STATUS: {
//					setStatus(LightStatus.fromValue(value));
//					break;
//				}
//				case LEVEL: { 
//					setLevel(Double.parseDouble(value));
//					break;
//				}
//				default: {
//					// TODO Exception
//				}
//			}
//		}
//	}
//}
