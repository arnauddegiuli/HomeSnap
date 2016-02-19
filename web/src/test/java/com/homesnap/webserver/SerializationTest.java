package com.homesnap.webserver;
/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
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

import org.junit.Test;

import com.homesnap.engine.controller.light.Light;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.house.utils.JSonTools;


public class SerializationTest {

	
	
	
	@Test
	public void toJsonTest() {
		
//		HouseServiceImpl service = new HouseServiceImpl();
		System.out.print(JSonTools.toJson(getModel()));
	}
	
	
	private House getModel() {
		House house = new House();
		
		Label label = new Label();
		label.setId("l1");
		label.setTitle("Label 1");
		house.getLabels().add(label);
		
		Light li = new Light();
		li.setTitle("toto");
		li.setWhere(new Where("12","12"));
		label.add(li);
			
		Light li2 = new Light();
		li2.setTitle("Light 2");
		li2.setWhere(new Where("13","13"));
		label.add(li2);
		
		label = new Label();
		label.setId("l2");
		label.setTitle("Label 2");
		house.getLabels().add(label);

		
		return house;
	}
}
