package com.homesnap.webserver.rest.listener;

import java.util.Map;

import com.homesnap.engine.house.House;


public class MyDomoPostListener extends MyDomoPutListener {

	public MyDomoPostListener(House house, String uri, Map<String, String[]> parameters, String body) {
		super(house, uri, parameters, body);
	}

	
}
