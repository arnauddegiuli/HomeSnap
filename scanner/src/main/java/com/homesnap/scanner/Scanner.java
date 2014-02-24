package com.homesnap.scanner;

/*
 * #%L
 * OpenWebNetScanner
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

import java.text.MessageFormat;

import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.connector.UnknownControllerListener;
import com.homesnap.engine.connector.openwebnet.OpenWebMonitorImpl;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;


public class Scanner {

	
	private Scanner(String host, int port, int password) {
		
		Monitor monitor = new OpenWebMonitorImpl(host, port, password);
		monitor.addUnknownControllerListener(new UnknownControllerListener() {

			@Override
			public void foundUnknownController(Who who, Where where, State what) {
				System.out.println(MessageFormat.format("Who [{0}] : Where [{1}] : what [{2}] : value [{3}] : {4}\n", who, where.getTo(), what.getName().getName(), what.getValue().getValue()));
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Scanner("192.168.1.35", 20000, 12345);
	}
}
