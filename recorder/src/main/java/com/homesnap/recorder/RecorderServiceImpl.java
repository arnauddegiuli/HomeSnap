package com.homesnap.recorder;

/*
 * #%L
 * HomeSnapRecorder
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

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.connector.UnknownControllerListener;
import com.homesnap.engine.connector.openwebnet.OpenWebMonitorImpl;
import com.homesnap.engine.controller.heating.HeatingZoneStateName;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

public class RecorderServiceImpl implements RecorderService {

	public dbUtils con = new dbUtils();

	public void init() {
 }
	
	public RecorderServiceImpl(String host, int port, int password) {
		init();
		Monitor monitor = new OpenWebMonitorImpl(host, port, password);
		monitor.addUnknownControllerListener(new UnknownControllerListener() {

			@Override
			public void foundUnknownController(Who who, Where where, List<What> whatList) {
				
				for (What what : whatList) {
					if(Who.HEATING_ADJUSTMENT.equals(who) & HeatingZoneStateName.MEASURE_TEMPERATURE.name().equals(what.getName())) {
						
						String whereStr = where != null ? where.getTo() : "null";
						String whatStr = what == null ? "null": what.getName() == null ? "null" : what.getName();
						String valueStr = what == null ? "null": what.getValue() == null ? "null" : what.getValue().toString();
						
						System.out.println(MessageFormat.format("Who [{0}] : Where [{1}] : what [{2}] : value [{3}]\n", who, whereStr, whatStr, valueStr));
						con.writeTemperatureData(whereStr, valueStr);
						
					}
				}
			}
		});
	}
	
	@Override
	public List<Record> getRecorderValueList() {
		return con.readData(null, null, null, null);
	}

	@Override
	public List<Record> getTypeValueList(String type) {
		return con.readData(type, null, null, null);
	}

	@Override
	public List<Record> getProbeValueList(String type, String probeId) {
		return con.readData(type, probeId, null, null);
	}

	@Override
	public List<Record> getDayValueList(String type, String probeId, int year,
			int month, int day) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Timestamp t = new Timestamp(c.getTimeInMillis());
		
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.MILLISECOND, 59);
		Timestamp t2 = new Timestamp(c.getTimeInMillis());
		
		return con.readData(type, probeId, t, t2);
	}

	@Override
	public List<Record> getMonthValueList(String type, String probeId,
			int year, int month) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp t = new Timestamp(c.getTimeInMillis());
		
		c.set(Calendar.MONTH, month+1);
		c.set(Calendar.DAY_OF_MONTH, -1);
		Timestamp t2 = new Timestamp(c.getTimeInMillis());
		
		return con.readData(type, probeId, t, t2);
	}

	@Override
	public List<Record> getYearValueList(String type, String probeId, int year) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, 1);
		Timestamp t = new Timestamp(c.getTimeInMillis());
		
		c.set(Calendar.MONTH, 12);
		Timestamp t2 = new Timestamp(c.getTimeInMillis());
		
		return con.readData(type, probeId, t, t2);
	}
}
