package com.homesnap.engine.services.impl;

import java.util.Vector;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;
import com.homesnap.engine.services.ControllerService;
import com.homesnap.engine.services.ScanListener;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final Vector<String> adressFound = new Vector<String>();
		
		ControllerService s = new OpenWebNetControllerService("127.0.1.1", 1234, 12345);
//		ControllerService s = new OpenWebNetControllerService("192.168.1.35", 20000, 12345);
		s.addScanListener(new ScanListener() {
			
			@Override
			public void progess(int percent) {
				System.out.println("Out: Percent - " + percent);
				if (percent == 100) {
					
					for(String adress : adressFound) {
						System.out.println(adress);
					}
				}
			}
			
			@Override
			public void foundController(Who who, Where where, Controller controller) {
				System.out.println("Out: who - " + who + "where - " + where.getFrom());
				adressFound.add(where.getFrom() + "-" + who);
			}
		});
		s.scan();

		
	}

}
