package com.adgsoftware.mydomo.engine.services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Application;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.PersistenceService;

public class PersistenceServiceImpl implements PersistenceService {

	private Application application;
	private ControllerService controllerService;
	
	public PersistenceServiceImpl(Application application, ControllerService controllerService) {
		this.application = application;
		this.controllerService = controllerService;
	}
	
	public void save(House house) throws IOException {

		FileOutputStream out = application.openFileOutput("house.xml", Application.MODE_PRIVATE);
	
		String s = serialized(house);
		out.write(s.getBytes());
		
		out.flush();
		out.close();
	}
	
	public House retrieve() throws IOException {
		House result;
		
		try {
			FileInputStream fileInput = application.openFileInput("house.xml");
			result = deserialized(fileInput);
			
		} catch (FileNotFoundException e) {
			result = null;
		}
		
		if (result == null) {
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
			backupCorruptedHouseFile();
			e.printStackTrace();
		} catch (SAXException e) {
			backupCorruptedHouseFile();
			e.printStackTrace();
		}
		return new House();
	}

	private void backupCorruptedHouseFile() throws FileNotFoundException,
			IOException {
		FileInputStream source = application.openFileInput("house.xml");
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		FileOutputStream backupFile = application.openFileOutput("corruptedHouseFile" + sd.format(new Date()) + ".xml", Application.MODE_WORLD_READABLE);

		int chara;
		while ( (chara = source.read()) != -1) {
			backupFile.write(chara);
		}
	}
}