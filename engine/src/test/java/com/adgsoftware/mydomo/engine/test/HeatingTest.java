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

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.ControllerDimensionChangeListener;
import com.adgsoftware.mydomo.engine.controller.DimensionStatus;
import com.adgsoftware.mydomo.engine.controller.DimensionStatusCallback;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingModeEnum;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.heating.Offset.Mode;
import com.adgsoftware.mydomo.engine.controller.heating.ValveStatusEnum;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.DesiredTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.MeasureTemperature;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.SetOffset;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.ValvesStatus;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.impl.ControllerServiceImpl;

public class HeatingTest {

	private ControllerService s = new ControllerServiceImpl("localhost", 1234, 12345);
	private Object lock = new Object();
	
	
	@Test
	public void getDesiredTemperatureSynchroneTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "10");
		getDesiredTemperatureSynchrone(hz);
		s.onDestroy();
	}
	
	public void getDesiredTemperatureSynchrone(HeatingZone hz) {
		System.out.println("getDesiredTemperatureSynchroneTest");
		DesiredTemperature d = hz.getDesiredTemperature(HeatingModeEnum.HEATING);
		Assert.assertNotNull(d);
		Assert.assertEquals(21, d.getDesiredTemperature(), 0);	
	}

	@Test
	public void setDesiredTemperatureTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "11");
		setDesiredTemperature(hz);
		s.onDestroy();
	}
	
	public void setDesiredTemperature(HeatingZone hz) {
		System.out.println("setDesiredTemperatureTest");
		hz.addControllerDimensionChangeListener(new ControllerDimensionChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onDimensionChangeError(
					ControllerDimension<? extends Status> controller,
					DimensionStatus newStatus, CommandResult result) {
				synchronized (lock) {
					System.out.println("Unlock desired temperature...");
					lock.notify();
				}
			}
			
			@Override
			public void onDimensionChange(
					ControllerDimension<? extends Status> controller,
					DimensionStatus newStatus) {

				synchronized (lock) {
					System.out.println("Unlock desired temperature...");
					lock.notify();
				}
			}
		});
		
		hz.setDesiredTemperature(19, HeatingModeEnum.HEATING);
		System.out.println("Wait desired temperature...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		DesiredTemperature r = hz.getDesiredTemperature(HeatingModeEnum.HEATING);
		Assert.assertNotNull(r);
		Assert.assertEquals(19, r.getDesiredTemperature(), 0);
		
		
		hz.setDesiredTemperature(21, HeatingModeEnum.HEATING);
		System.out.println("Wait desired temperature...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		r = hz.getDesiredTemperature(HeatingModeEnum.HEATING);
		Assert.assertNotNull(r);
		Assert.assertEquals(21, r.getDesiredTemperature(), 0);

	}
	
	
	
	private DesiredTemperature d = null;
	
	@Test
	public void getDesiredTemperatureAsynchroneTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "10");
		getDesiredTemperatureAsynchrone(hz);
		s.onDestroy();
	}
	
	public void getDesiredTemperatureAsynchrone(HeatingZone hz) {
		System.out.println("getDesiredTemperatureAsynchroneTest");
		DimensionStatusCallback<DesiredTemperature> callback = new DimensionStatusCallback<DesiredTemperature>() {
			public void value(DesiredTemperature value) {
				d=value;
				synchronized (lock) {
					System.out.println("Unlock desired temperature async...");
					lock.notify();
				}
			}
		};
		
		hz.getDesiredTemperature(HeatingModeEnum.HEATING, callback);
		System.out.println("Wait desired temperature async...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(d);
		Assert.assertEquals(21, d.getDesiredTemperature(), 0);
	}
	
	private MeasureTemperature m = null;
	
	@Test
	public void getMeasureTemperatureTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "10");
		getMeasureTemperature(hz);
		s.onDestroy();
	}
	
	public void getMeasureTemperature(HeatingZone hz) {
		System.out.println("getMeasureTemperatureTest");
		DimensionStatusCallback<MeasureTemperature> callback = new DimensionStatusCallback<MeasureTemperature>() {
			public void value(MeasureTemperature value) {
				m=value;
				synchronized (lock) {
					System.out.println("Unlock measure temperature...");
					lock.notify();
				}
			}
		};
		
		hz.getMeasureTemperature(callback);
		System.out.println("Wait measure temperature...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(m);
		Assert.assertEquals(18, m.getMeasuredTemperature(), 0);
	}

	private ValvesStatus v = null;
	
	@Test
	public void getValvesStatusTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "10");
		getValvesStatus(hz);
		s.onDestroy();
	}
	
	public void getValvesStatus(HeatingZone hz) {
		System.out.println("getValvesStatus");
		DimensionStatusCallback<ValvesStatus> callback = new DimensionStatusCallback<ValvesStatus>() {
			public void value(ValvesStatus value) {
				v=value;
				synchronized (lock) {
					System.out.println("Unlock valve status...");
					lock.notify();
				}
			}
		};
		
		hz.getValvesStatus(callback);
		System.out.println("Wait valve status...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(v);
		Assert.assertEquals(v.getHeatingValveStatus(), ValveStatusEnum.OFF);
	}
	
	private SetOffset offset = null;
	
	@Test
	public void getSetOffsetTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "10");
		getSetOffset(hz);
		s.onDestroy();
	}
	
	public void getSetOffset(HeatingZone hz) {
		System.out.println("getSetOffsetTest");
		DimensionStatusCallback<SetOffset> callback = new DimensionStatusCallback<SetOffset>() {
			public void value(SetOffset value) {
				offset=value;
				synchronized (lock) {
					System.out.println("Unlock offset...");
					lock.notify();
				}
			}
		};
		
		hz.getSetOffset(callback);
		System.out.println("Wait offset...");
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(offset);
		Assert.assertEquals(0, offset.getLocalOffset().getDegree());
		Assert.assertEquals(Mode.OFF, offset.getLocalOffset().getMode());
	}
	
	@Test
	public void allTest() {
		HeatingZone hz = s.createController(HeatingZone.class, "12");
		this.getDesiredTemperatureAsynchrone(hz);
		this.getDesiredTemperatureSynchrone(hz);
		this.getMeasureTemperature(hz);
		this.getSetOffset(hz);
		this.getValvesStatus(hz);
		this.setDesiredTemperature(hz);
		s.onDestroy();
	}

}
