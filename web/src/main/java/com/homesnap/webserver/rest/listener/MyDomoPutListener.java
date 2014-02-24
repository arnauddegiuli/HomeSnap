package com.homesnap.webserver.rest.listener;

import java.util.Hashtable;
import java.util.Map;

import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.ControllerChangeListener;
import com.homesnap.engine.controller.light.Light;
import com.homesnap.engine.controller.light.Light.LightStateValue;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.house.House;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.impl.OpenWebNetControllerService;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.Verb;



public class MyDomoPutListener extends MyDomoRestListenerAbstract implements MyDomoRestAPI {

	private ControllerService service = new OpenWebNetControllerService("localhost", 1234, 12345);
	private Map<String, Light> lightList = new Hashtable<String, Light>();

	private String body;
	
	public MyDomoPutListener(House house, String uri, Map<String, String[]> parameters, String body) {
		super(house, uri, parameters);
		this.body = body;
	}

	@Override
	public void onHouse() {
		// TODO mettre à jour
//		House newHouse = JSonTools.fromJson(body);
//		getHouse().setGroups(newHouse.getGroups());
//		getHouse().setLabels(newHouse.getLabels());
	}

	@Override
	public void onLabelList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onLabel(String labelId) {
		// TODO update label
	}

	@Override
	public void onControllerByLabel(String labelId, String where) {
		// TODO update controller
	}

	@Override
	public void onGroupList() throws UnsupportedRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onGroup(String groupId) {
		// TODO update group
	}

	@Override
	public void onControllerByGroup(String groupId, String where) {
		// TODO update controller
		System.out.println("UPDATE group=[" + groupId + "] with where=[" + where + "]");
		System.out.println("Body=[" + body + "]");
		
//		Controller<?> c = JSonTools.ControllerFromJson(service, body);
//		getGroup(groupId).add(c);
	}

	@Override
	public void onController(String where) throws UnsupportedRestOperation {
		// TODO update controller
//		public Light putLight(String adress, String title) {
//			Light result = getLight(adress);
//			
//			if (result == null) { // creation
//				result = service.createController(Light.class, adress);
//			} 
//			result.setTitle(title);
//			return result;
//		}
	}
	
	@Override
	public void onLightStatus(String where, LightStateValue status) throws RestOperationException {
		
		Light l = getLight(where);
		synchronized (this) {
			if (LightStateValue.LIGHT_ON.equals(status)) {
				l.setStatus(LightStateValue.LIGHT_ON);
			} else if (LightStateValue.LIGHT_OFF.equals(status)) {
				l.setStatus(LightStateValue.LIGHT_OFF);
			}
			
			try {
				this.wait(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setResult(l.toJson().toString());
		}
	}

	private Light getLight(String adress) {
		Light result = lightList.get(adress);
		if(result == null) {
			result = service.createController(Light.class, adress);
			result.addControllerChangeListener(new ControllerChangeListener() {
				
				@Override
				public void onWhatChangeError(Controller controller,
						State oldStatus, State newStatus, CommandResult result) {
					synchronized (this) {
						this.notify();
					}
				}
				
				@Override
				public void onWhatChange(Controller controller,
						State oldStatus, State newStatus) {
					synchronized (this) {
						this.notify();
					}
				}
			});
			
			lightList.put(adress, result);
		}
		return result;
	}

}
