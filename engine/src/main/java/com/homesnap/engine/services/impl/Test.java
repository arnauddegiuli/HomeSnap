package com.homesnap.engine.services.impl;

import com.homesnap.engine.services.ControllerService;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ControllerService s = new OpenWebNetControllerService("192.168.1.35", 20000, 12345);
		s.scan();
	}

}
