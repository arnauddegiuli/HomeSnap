package com.homesnap.engine.test;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;

import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.ControllerChangeListener;
import com.homesnap.engine.controller.light.Light;
import com.homesnap.engine.controller.light.stateValue.LightStatusValue;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.impl.OpenWebNetControllerService;

public class LightTest {

	private ControllerService s = new OpenWebNetControllerService("localhost", 1234, 12345);
	private Object lock = new Object();
	
	@Test
	public void statusOnOffTest() {
		
		final Light light = s.createController(Light.class, "12");
		
		// Listener will make us availabe to wait response from server
		light.addControllerChangeListener(new ControllerChangeListener() {

			@Override
			public void onStateChangeError(Controller controller,
					State oldStatus, State newStatus, CommandResult result) {
				synchronized (lock) {
					// When response from server is here we unlock the thread
					System.out.println("Unlock...");
					lock.notify();
				}
			}
			
			@Override
			public void onStateChange(Controller controller,
					State oldStatus, State newStatus) {
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
			e.printStackTrace();
		}

		// By default server send back a OFF status. If value == null, it is a bug or just server have not enough time (1 second) to respond
		Assert.assertNotNull(light.getStatus());
		Assert.assertEquals(LightStatusValue.LIGHT_OFF , light.getStatus());

		// Now set the value to ON
		light.setStatus(LightStatusValue.LIGHT_ON);
		// Wait the response from the server
		try {
			synchronized (lock) {
				System.out.println("Wait...");	
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Check that after the server response now the status is ON
		Assert.assertNotNull(light.getStatus());
		Assert.assertEquals(LightStatusValue.LIGHT_ON , light.getStatus());

		// Switch off again
		light.setStatus(LightStatusValue.LIGHT_OFF);
		try {
			synchronized (lock) {
				System.out.println("Wait...");
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(light.getStatus());
		Assert.assertEquals(LightStatusValue.LIGHT_OFF , light.getStatus());
		
		System.out.println("Finish...");

	}
}
