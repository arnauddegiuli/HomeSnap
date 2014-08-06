package com.homesnap.webserver.rest.listener;

import java.util.Map;

import com.homesnap.engine.house.House;

// Creation
public class MyDomoPostListener extends MyDomoPutListener {

	public MyDomoPostListener(House house, String uri, Map<String, String[]> parameters, String body) {
		super(house, uri, parameters, body);
	}

//	@Override
//	public void onLightStatus(String where, LightStatusValue status) throws RestOperationException {
//		
//		Light l = getLight(where);
//		synchronized (this) {
//			if (LightStatusValue.LIGHT_ON.equals(status)) {
//				l.setStatus(LightStatusValue.LIGHT_ON);
//			} else if (LightStatusValue.LIGHT_OFF.equals(status)) {
//				l.setStatus(LightStatusValue.LIGHT_OFF);
//			}
//			
//			try {
//				this.wait(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			setResult(l.toJson().toString());
//		}
//	}

//	private Light getLight(String adress) {
//		Light result = lightList.get(adress);
//		if(result == null) {
//			result = service.createController(Light.class, adress);
//			result.addControllerChangeListener(new ControllerChangeListener() {
//				
//				@Override
//				public void onStateChangeError(Controller controller,
//						State oldStatus, State newStatus, CommandResult result) {
//					synchronized (this) {
//						this.notify();
//					}
//				}
//				
//				@Override
//				public void onStateChange(Controller controller,
//						State oldStatus, State newStatus) {
//					synchronized (this) {
//						this.notify();
//					}
//				}
//			});
//			
//			lightList.put(adress, result);
//		}
//		return result;
//	}

}
