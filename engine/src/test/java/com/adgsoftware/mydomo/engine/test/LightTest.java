package com.adgsoftware.mydomo.engine.test;

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.test.utils.ControllerServiceImpl;

public class LightTest {

	private ControllerService s = new ControllerServiceImpl("localhost", 1234);
	private Object lock = new Object();
	
	@Test
	public void statusOnOffTest() {
		
		final Light light = s.createController(Light.class, "12");
		
		// Listener will make us availabe to wait response from server
		light.addControllerChangeListener(new ControllerChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChangeError(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus, CommandResult result) {
				synchronized (lock) {
					// When response from server is here we unlock the thread
					System.out.println("Unlock...");
					lock.notify();
				}
			}
			
			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				synchronized (lock) {
					// When response from server is here we unlock the thread
					System.out.println("Unlock...");
					lock.notify();
				}
			}
		});
		
		
		// First we just wait 1 second to be sure the controller is initialize 
		try {
			synchronized (lock) {
				
				lock.wait(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// By default server send back a OFF status. If value == null, it is a bug or just server have not enough time (1 second) to respond
		Assert.assertNotNull(light.getWhat());
		Assert.assertEquals(LightStatus.LIGHT_OFF , light.getWhat());
		
		// Now set the value to ON
		light.setWhat(LightStatus.LIGHT_ON);
		System.out.println("Wait...");
		
		// Wait the response from the server
		try {
			synchronized (lock) {
				
				lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Check that after the server response now the status is ON
		Assert.assertNotNull(light.getWhat());
		Assert.assertEquals(LightStatus.LIGHT_ON , light.getWhat());
		
		// Switch off again
		light.setWhat(LightStatus.LIGHT_OFF);
		System.out.println("Wait...");
		
		try {
			synchronized (lock) {
				
				lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertNotNull(light.getWhat());
		Assert.assertEquals(LightStatus.LIGHT_OFF , light.getWhat());
		
		System.out.println("Finish...");

		
	}
}
