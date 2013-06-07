package com.adgsoftware.mydomo.engine.test;

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

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.parser.CommandParser;
import com.adgsoftware.mydomo.engine.parser.ParseException;

public class ParserTest {
	
	@Test
	public void whoTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		System.out.println(p.getWho()); // Should display 1
		Assert.assertEquals(p.getWho(), "1");
		
		// Space case..
		String gatewayStatusCommand = "*#13**0##";
		p  = CommandParser.parse(gatewayStatusCommand);
		System.out.println(p.getWho()); // Should display 13
		Assert.assertEquals(p.getWho(), "13");

	}
	
	@Test
	public void whereTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		System.out.println(p.getWhere()); // Should display 12
		Assert.assertEquals(p.getWhere(), "12");
	}
	
	@Test
	public void statusTest() throws ParseException {
		// Status request    *#WHO*WHERE##
		String status = "*#1*12##";
		CommandParser p  = CommandParser.parse(status);
		System.out.println(p.getWhere()); // Should display 12;
		Assert.assertEquals(p.getWhere(), "12");
	}
	
	@Test
	public void dimensionRequestTest() throws ParseException {
		// Dimension request *#WHO*WHERE*DIMENSION##tring dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		String dimensionStatus = "*#12*22*0##";
		CommandParser p  = CommandParser.parse(dimensionStatus);
		System.out.println(p.getDimension()); // Should display 0
		Assert.assertEquals(p.getDimension(), "0");
	}
	
	@Test
	public void dimensionWriteTest() throws ParseException {
		// Dimension write   *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##
		String dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		CommandParser p  = CommandParser.parse(dimensionCommand);
		System.out.println(p.getDimension()); // Should display 1
		Assert.assertEquals(p.getDimension(), "1");
	}
}
