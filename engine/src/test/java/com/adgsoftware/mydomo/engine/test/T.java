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


import com.adgsoftware.mydomo.engine.oldcontroller.light.Light;
import com.adgsoftware.mydomo.engine.oldcontroller.light.Light.LightStatus;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.impl.OpenWebNetControllerService;

public class T {

	private ControllerService s = new OpenWebNetControllerService("192.168.1.35", 20000, 12345);

	
	public static void main(String[] args) {
		new T().statusOnOff();
	}
	
	public void statusOnOff() {
		
		final Light light = s.createController(Light.class, "42");
		
		light.setWhat(LightStatus.LIGHT_OFF);

		
	}
}
