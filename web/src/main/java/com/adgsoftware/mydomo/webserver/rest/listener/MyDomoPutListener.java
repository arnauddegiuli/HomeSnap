package com.adgsoftware.mydomo.webserver.rest.listener;

import java.util.Hashtable;
import java.util.Map;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.impl.ControllerServiceImpl;
import com.adgsoftware.mydomo.webserver.rest.MyDomoRestAPI;
import com.adgsoftware.mydomo.webserver.rest.RestOperationException;
import com.adgsoftware.mydomo.webserver.rest.UnsupportedRestOperation;
import com.adgsoftware.mydomo.webserver.rest.Verb;
import com.adgsoftware.mydomo.webserver.utils.JSonTools;



public class MyDomoPutListener extends MyDomoRestListenerAbstract implements MyDomoRestAPI {

	private ControllerService service = new ControllerServiceImpl("localhost", 1234, 12345);
	private Map<String, Light> controllerList = new Hashtable<String, Light>();

	public MyDomoPutListener(House house, String uri, Map<String, String[]> parameters) {
		super(house, uri, parameters);
	}

	@Override
	public void onHouse() {
		// TODO mettre à jour
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
	}

	@Override
	public void onController(String where) throws UnsupportedRestOperation {
		// TODO update controller
	}
	
	@Override
	public void onLightStatus(String where, LightStatus status) throws RestOperationException {
		setResult(JSonTools.toJson(putCommand(where, status)));
	}
	
	public Light putLight(String adress, String title) {
		Light result = getController(adress);
		
		if (result == null) { // creation
			result = service.createController(Light.class, adress);
		} 
		result.setTitle(title);
		return result;
	}
	
	private Light putCommand(String adress, LightStatus status) {
		
		Light l = getController(adress);
		synchronized (this) {
			if (LightStatus.LIGHT_ON.equals(status)) {
				l.setWhat(LightStatus.LIGHT_ON);
			} else if (LightStatus.LIGHT_OFF.equals(status)) {
				l.setWhat(LightStatus.LIGHT_OFF);
			}
			
			try {
				this.wait(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return l;
	}

	private Light getController(String adress) {
		Light result = controllerList.get(adress);
		if(result == null) {
			result = service.createController(Light.class, adress);
			result.addControllerChangeListener(new ControllerChangeListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void onWhatChangeError(Controller<? extends Status> controller,
						Status oldStatus, Status newStatus, CommandResult result) {
					synchronized (this) {
						this.notify();
					}
				}
				
				@Override
				public void onWhatChange(Controller<? extends Status> controller,
						Status oldStatus, Status newStatus) {
					synchronized (this) {
						this.notify();
					}
				}
			});
			
			controllerList.put(adress, result);
		}
		return result;
	}
	
	// /house/label
//	public Label putLabel(String id, String title) {
//		Label label = getLabel(id);
//		if (label == null) {
//			// Creation
//			label = new Label();
//			readHouse().getLabels().add(label);
//		} 
//		label.setTitle(title);
//		// Save
//		saveHouse(house);
//		return label;
//	}

}
