package com.adgsoftware.mydomo.engine.test;

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

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.automation.Automation.AutomationStatus;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.impl.ControllerServiceImpl;

public class AutomationTest {

	private ControllerService s = new ControllerServiceImpl("localhost", 1234, 12345);
	private Object lock = new Object();
	
	@Test
	public void statusUpDownTest() {
		
		final Automation automation = s.createController(Automation.class, "32");
		
		// Listener will make us availabe to wait response from server
		automation.addControllerChangeListener(new ControllerChangeListener() {
			
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
			e.printStackTrace();
		}
		
		// By default server send back a OFF status. If value == null, it is a bug or just server have not enough time (1 second) to respond
		Assert.assertNotNull(automation.getWhat());
		Assert.assertEquals(AutomationStatus.AUTOMATION_STOP , automation.getWhat());
		
		// Now set the value to AUTOMATION_DOWN
		automation.setWhat(AutomationStatus.AUTOMATION_DOWN);
		System.out.println("Wait...");
		
		// Wait the response from the server
		try {
			synchronized (lock) {
				
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Check that after the server response now the status is ON
		Assert.assertNotNull(automation.getWhat());
		Assert.assertEquals(AutomationStatus.AUTOMATION_DOWN , automation.getWhat());
		
		// Switch UP now again
		automation.setWhat(AutomationStatus.AUTOMATION_UP);
		System.out.println("Wait...");
		
		try {
			synchronized (lock) {
				
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(automation.getWhat());
		Assert.assertEquals(AutomationStatus.AUTOMATION_UP , automation.getWhat());

		// Switch OFF now again
		automation.setWhat(AutomationStatus.AUTOMATION_STOP);
		System.out.println("Wait...");
		
		try {
			synchronized (lock) {
				
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(automation.getWhat());
		Assert.assertEquals(AutomationStatus.AUTOMATION_STOP , automation.getWhat());

		
		System.out.println("Finish...");

		
	}
}
