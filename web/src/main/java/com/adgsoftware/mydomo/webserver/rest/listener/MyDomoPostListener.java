package com.adgsoftware.mydomo.webserver.rest.listener;

import java.util.Map;

import com.adgsoftware.mydomo.engine.house.House;


public class MyDomoPostListener extends MyDomoPutListener {

	public MyDomoPostListener(House house, String uri, Map<String, String[]> parameters) {
		super(house, uri, parameters);
	}

	
}
