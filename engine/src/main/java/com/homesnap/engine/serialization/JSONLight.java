package com.homesnap.engine.serialization;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import com.homesnap.engine.controller.light.LightController;
import com.homesnap.engine.controller.light.LightStatus;
import com.homesnap.engine.controller.state.StateName;
import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class JSONLight extends LightController implements Externalizable {

	/**
	 * 
	 */
	public JSONLight() {
		this(null);
	}

	/**
	 * 
	 * @param address
	 */
	public JSONLight(String address) {
		super(address);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		
		JSONObject jsonController = new JSONObject();
		jsonController.put("name", getName());
		jsonController.put("description", getDescription());
		jsonController.put("address", getAddress());
		
		JSONObject jsonStates = new JSONObject();
		for(Entry<StateName, StateValue> state : getStates().entrySet()) {
			jsonStates.put(state.getKey().getName(), state.getValue().getValue());
		}
		jsonController.put("states", jsonStates);
		out.writeObject(jsonController.toString());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		
		JSONObject jsonController = new JSONObject((String) in.readObject());
		setName(jsonController.getString("name"));
		setDescription(jsonController.getString("description"));
		setAddress(jsonController.getString("address"));
		
		JSONObject jsonStates = jsonController.getJSONObject("states");
		@SuppressWarnings("unchecked")
		Set<String> states = (Set<String>) jsonStates.keySet();
		for (String stateKey : states) {
			
			StateName state = StateName.fromValue(stateKey);
			String value = jsonStates.getString(stateKey);
			
			switch (state) {
				case STATUS: {
					setStatus(LightStatus.fromValue(value));
					break;
				}
				case LEVEL: { 
					setLevel(Double.parseDouble(value));
					break;
				}
				default: {
					// TODO Exception
				}
			}
		}
	}
}
