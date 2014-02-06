package com.adgsoftware.mydomo.engine.services.impl;

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


import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.engine.oldcontroller.Controller;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.PersistenceService;

public class PersistenceServiceImpl implements PersistenceService {

	private ControllerService controllerService;

	public PersistenceServiceImpl(ControllerService controllerService) {
		this.controllerService = controllerService;
	}

	@Override
	public void save(House house, OutputStream file) throws IOException {
		file.write(serialized(house).getBytes());
		file.flush();
		file.close();
	}

	@Override
	public House retrieve(InputStream file) throws IOException {
		House result;
		try {
			result = deserialized(file);
		} catch (FileNotFoundException e) {
			result = new House();
		}

		if (result.getGroups().size() == 0) {
			List<Group>groups = result.getGroups();
			// Now it is here but later when support knx or other protocole get it out
			for(int i = 0; i < 10; i++) {
				createGroup(groups, i);
			}
		}
		return result;
	}

	private void createGroup(List<Group> groups, int number) {
		Group group = new Group();
		group.setTitle("Group " + number);
		group.setId("" + number);
		groups.add(group);
	}

	private String serialized(House house) {
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<house>");
		// Save physical model
		sb.append("<groups>");
		for (Group label : house.getGroups()) {
			sb.append("<group title=\"");
			sb.append(label.getTitle());
			sb.append("\" id=\"");
			sb.append(label.getId());
			sb.append("\">");
			// Save controller
			for (Controller<? extends Status> controller : label.getControllerList()) {
				// Save address
				sb.append("<controller where=\"");
				if (controller.getWhere() != null) {
					sb.append(controller.getWhere());
				}
				// Save title
				sb.append("\" title=\"");
				if (controller.getTitle() != null) {
					sb.append(controller.getTitle());
				}
				// Save controller type
				sb.append("\" class=\"")
				.append(controller.getClass().getName())
				.append("\"/>");				
			}
			sb.append("</group>");
		}
		sb.append("</groups>");
		
		// Save logical model
		sb.append("<labels>");
		for (Label label : house.getLabels()) {
			sb.append("<label title=\"");
			sb.append(label.getTitle());
			sb.append("\" id=\"");
			sb.append(label.getId());
			sb.append("\">");
			for (Controller<? extends Status> controller : label) {
				
				sb.append("<controllerLink where=\"");
				
				if (controller.getWhere() != null) {
					sb.append(controller.getWhere());
				}
								
				sb.append("\"/>");
			}
			sb.append("</label>");
		}
		sb.append("</labels>");
		sb.append("</house>");
		return sb.toString();
		
	}
	
	private class MyHandler extends DefaultHandler {

		private House house = new House();
		private Group currentGroup;
		private Label currentLabel;
		private Hashtable<String, Controller<? extends Status>> controllerList = new Hashtable<String, Controller<? extends Status>>();

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			// Load physical model
			if ("group".equals(qName)) {
				currentGroup = new Group();
				String title =  attributes.getValue("title");
				String id =  attributes.getValue("id");
				currentGroup.setTitle(title);
				currentGroup.setId(id);
				house.getGroups().add(currentGroup);
			}

			if ("controller".equals(qName)) {
				try {
					String where = attributes.getValue("where");
					String clazzStr = attributes.getValue("class");
					String title =  attributes.getValue("title");
					@SuppressWarnings("unchecked")
					Class<? extends Controller<?>> clazz = (Class<? extends Controller<?>>) Class.forName(clazzStr);
					Controller<?> c = (Controller<?>) controllerService.createController(clazz, where);
					c.setTitle(title);
					currentGroup.add(c);
					controllerList.put(c.getWhere(), c);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			// Load logical model
			if ("label".equals(qName)) { // TODO manage sub label
				currentLabel = new Label();
				String title =  attributes.getValue("title");
				String id =  attributes.getValue("id");
				currentLabel.setTitle(title);
				currentLabel.setId(id);
				house.getLabels().add(currentLabel);
			}

			if ("controllerLink".equals(qName)) {
				String where = attributes.getValue("where");
				currentLabel.add(controllerList.get(where));
			}
		}

		public House getHouse() {
			return house;
		}
	}

	private House deserialized(InputStream in) throws IOException {
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			MyHandler m = new MyHandler();
			parser.parse(in, m);
			return m.getHouse();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return new House();
	}
}