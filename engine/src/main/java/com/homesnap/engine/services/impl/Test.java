package com.homesnap.engine.services.impl;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.ScanListener;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final List<String> adressFound = new ArrayList<String>();
		
		ControllerService s = new OpenWebNetControllerService("192.168.1.35", 20000, 12345);
		s.addScanListener(new ScanListener() {
			
			@Override
			public void progess(int percent) {
				System.out.println("Out: Percent - " + percent);
				if (percent == 100) {
					
					for(String s2 : adressFound) {
						System.out.println(s2);
					}
				}
			}
			
			@Override
			public void foundController(Who who, Where where, Controller controller) {
				System.out.println("Out: who - " + who + "where - " + where.getFrom());
				adressFound.add(where.getFrom() + "-" + who);
				for(String s2 : adressFound) {
					System.out.println(s2);
				}
			}
		});
		s.scan();

		
	}

}
