package com.adgsoftware.mydomo.engine.test;

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.ControllerDimensionChangeListener;
import com.adgsoftware.mydomo.engine.controller.DimensionStatus;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingModeEnum;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.test.utils.ControllerServiceImpl;

public class HeatingTest {

	private ControllerService s = new ControllerServiceImpl("localhost", 1234);
	private Object lock = new Object();
	
	@Test
	public void test() {
		
		final HeatingZone hz = s.createController(HeatingZone.class, "10");
		Double d = hz.getDesiredTemperature(HeatingModeEnum.HEATING);
		Assert.assertNotNull(d);
		Assert.assertEquals(d, 21, 0);
		
		hz.addControllerDimensionChangeListener(new ControllerDimensionChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onDimensionChangeError(
					ControllerDimension<? extends Status> controller,
					DimensionStatus newStatus, CommandResult result) {
				synchronized (lock) {
					System.out.println("Unlock...");
					lock.notify();
				}
			}
			
			@Override
			public void onDimensionChange(
					ControllerDimension<? extends Status> controller,
					DimensionStatus newStatus) {

				synchronized (lock) {
					System.out.println("Unlock...");
					lock.notify();
				}
			}
		});
		
		hz.setDesiredTemperature(19, HeatingModeEnum.HEATING);
		System.out.println("Wait...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Double r = hz.getDesiredTemperature(HeatingModeEnum.HEATING);
		Assert.assertNotNull(r);
		Assert.assertEquals(r, 19, 0);
		
		System.out.println("Finish...");

		
	}
}
