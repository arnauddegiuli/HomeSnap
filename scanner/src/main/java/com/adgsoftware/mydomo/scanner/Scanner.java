package com.adgsoftware.mydomo.scanner;

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
import java.util.List;

import com.adgsoftware.mydomo.engine.connector.Monitor;
import com.adgsoftware.mydomo.engine.connector.UnknownControllerListener;
import com.adgsoftware.mydomo.engine.connector.impl.OpenWebMonitorImpl;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;


public class Scanner {

	
	private Scanner(String host, int port, int password) {
		
		Monitor monitor = new OpenWebMonitorImpl(host, port, password);
		monitor.addUnknownControllerListener(new UnknownControllerListener() {
			
			@Override
			public void foundUnknownController(String who, String where, String what,
					String dimensionCode, List<DimensionValue> dimensionList) {
				
				StringBuilder sb = new StringBuilder();
				
				for (DimensionValue dimValue : dimensionList) {
					sb.append("\t\t - value[").append(dimValue.getValue()).append("]\n");
				}
				
				System.out.println(MessageFormat.format("Who [{0}] : Where [{1}] : what [{2}] : dimensionCode [{3}] : dimension\n", who, where, what, dimensionCode, sb.toString()));
				
			}
		});

		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Scanner("localhost", 1234, 12345);
	}

	
	
}
