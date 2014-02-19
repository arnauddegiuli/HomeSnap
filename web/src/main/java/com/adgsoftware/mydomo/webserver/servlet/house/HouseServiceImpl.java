package com.adgsoftware.mydomo.webserver.servlet.house;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.where.Where;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Icon;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.engine.services.PersistenceService;
import com.adgsoftware.mydomo.engine.services.impl.OpenWebNetControllerService;
import com.adgsoftware.mydomo.engine.services.impl.PersistenceServiceImpl;

/*
 * #%L
 * MyDomoWebServer
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


public class HouseServiceImpl {

	private static Object lock = new String();
	private static House house = null;
	private PersistenceService persistence = new PersistenceServiceImpl(new OpenWebNetControllerService("localhost", 1234, 12345));

	public void saveHouse(House house) {
		synchronized (lock) {
			if (house != null) {
					try {
						persistence.save(house, new FileOutputStream("house.xml"));
					} catch (FileNotFoundException e) {
						// TODO log
						e.printStackTrace();
					} catch (IOException e) {
						// TODO log
						e.printStackTrace();
					}
				
			}
		}
	}

	public House readHouse() {
		synchronized (lock) {
			if (house == null) {
				try {
					house = persistence.retrieve(new FileInputStream("house.xml"));
					if (house == null) {
						house = buildTempHouse();
					}
				} catch (FileNotFoundException e) {
//					// TODO backup
					house = buildTempHouse();
				} catch (IOException e) {
//					// TODO backup
					house = buildTempHouse();
				}
			}
			return house;
		}
	}

	private House buildTempHouse() {
		house = new House();
		
		Label label = new Label();
		label.setId("ch1");
		label.setTitle("Chambre 1");
		label.setIcon(Icon.chamber);
		house.getLabels().add(label);

		Light li = new Light();
		li.setTitle("toto");
		li.setWhere(new Where("12", "12"));
		label.add(li);

		Light li2 = new Light();
		li2.setTitle("Light 2");
		li2.setWhere(new Where("13","13"));
		label.add(li2);

		Light li3 = new Light();
		li3.setTitle("Light 3");
		li3.setWhere(new Where("14","14"));
		label.add(li3);

		label = new Label();
		label.setId("ch2");
		label.setTitle("Chambre 2");
		label.setIcon(Icon.chamber);
		house.getLabels().add(label);

		Light li4 = new Light();
		li4.setTitle("Light ch2");
		li4.setWhere(new Where("15","15"));
		label.add(li4);

		label = new Label();
		label.setId("cui");
		label.setTitle("Cuisine");
		label.setIcon(Icon.chamber);
		house.getLabels().add(label);

		Light li5 = new Light();
		li5.setTitle("Light Cuisine");
		li5.setWhere(new Where("16", "16"));
		label.add(li5);

		Group group1 = new Group();
		group1.setId("1");
		group1.setTitle("Group 1");
		group1.add(li);
		group1.add(li2);
		group1.add(li3);
		group1.add(li4);
		group1.add(li5);
		house.getGroups().add(group1);

		return house;
	}
}